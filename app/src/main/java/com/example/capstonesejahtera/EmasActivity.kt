package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.view.View


class EmasActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var totalTextView: TextView
    private lateinit var totalCatatanTextView: TextView
    private lateinit var lineChart: LineChart
    private lateinit var recommendationTextView: TextView  // Add this line

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emas)

        firestore = Firebase.firestore
        totalTextView = findViewById(R.id.tabungantotalemas)
        totalCatatanTextView = findViewById(R.id.totalcatetanemas)
        lineChart = findViewById(R.id.lineChart)
        recommendationTextView = findViewById(R.id.recommendation) // Initialize the recommendation TextView

        // Sembunyikan elemen yang tidak perlu ditampilkan
        totalTextView.visibility = View.GONE
        totalCatatanTextView.visibility = View.GONE
        // Sembunyikan rekomendasi di awal
        recommendationTextView.visibility = View.GONE

        fetchTabunganData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.emas)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchTabunganData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userTabungan = firestore.collection("Tabungan").document(userId).collection("user_data")
                    val documents = userTabungan.get().await()

                    var totalNominal = 0.0
                    for (document in documents) {
                        val nominal = document.getDouble("nominal") ?: 0.0
                        totalNominal += nominal
                    }

                    launch(Dispatchers.Main) {
                        displayTotalNominal(totalNominal)
                        fetchCatatanData(totalNominal)
                    }
                } catch (e: Exception) {
                    Log.e("EmasActivity", "Error fetching tabungan data: ", e)
                    showErrorMessage("Gagal memuat data tabungan.")
                }
            }
        } else {
            Log.w("EmasActivity", "User is not authenticated.")
        }
    }

    private fun fetchCatatanData(totalNominal: Double) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userCatatan = firestore.collection("Catatan").document(userId).collection("user_data")
                    val documents = userCatatan.get().await()

                    var totalCatatanNominal = 0.0
                    for (document in documents) {
                        val nominal = document.getDouble("nominal") ?: 0.0
                        totalCatatanNominal += nominal
                    }

                    launch(Dispatchers.Main) {
                        displayTotalCatatanNominal(totalCatatanNominal)
                        callApiForPrediction(totalNominal, totalCatatanNominal)
                    }
                } catch (e: Exception) {
                    Log.e("EmasActivity", "Error fetching catatan data: ", e)
                    showErrorMessage("Gagal memuat data catatan.")
                }
            }
        } else {
            Log.w("EmasActivity", "User is not authenticated.")
        }
    }

    private fun callApiForPrediction(totalNominal: Double, totalCatatanNominal: Double) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://emasdansaham-810319962197.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val request = PredictionRequest(
            income = totalNominal,
            expenses = totalCatatanNominal
        )

        apiService.predictGold(request).enqueue(object : Callback<EmasResponse> {
            override fun onResponse(call: Call<EmasResponse>, response: Response<EmasResponse>) {
                if (response.isSuccessful) {
                    val predictedPrices = response.body()?.predictedPrices
                    displayPredictedPrices(predictedPrices)
                    val recommendation = response.body()?.recommendation  // Get recommendation from the response
                    displayRecommendation(recommendation)  // Display the recommendation
                } else {
                    Log.e("EmasActivity", "Error response from API: ${response.errorBody()}")
                    showErrorMessage("Gagal mendapatkan prediksi harga emas.")
                }
            }

            override fun onFailure(call: Call<EmasResponse>, t: Throwable) {
                Log.e("EmasActivity", "API call failed: ", t)
                showErrorMessage("Koneksi ke server gagal.")
            }
        })
    }

    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(value).replace("Rp", "Rp ").replace(",00", "")
    }

    private fun displayTotalNominal(total: Double) {
        totalTextView.text = "Total Tabungan: ${formatRupiah(total)}"
    }

    private fun displayTotalCatatanNominal(total: Double) {
        totalCatatanTextView.text = "Total Catatan: ${formatRupiah(total)}"
    }

    private fun displayPredictedPrices(predictedPrices: List<Any?>?) {
        if (predictedPrices == null || predictedPrices.isEmpty()) {
            lineChart.clear()
            lineChart.invalidate()
            findViewById<TextView>(R.id.titihargaterakhir).text = "Data tidak tersedia"
            return
        }

        // Urutkan nilai prediksi berdasarkan harga (opsional, jika diperlukan)
        val sortedPrices = predictedPrices.mapNotNull { it as? Double }.sorted()

        // Konversi nilai prediksi menjadi entri untuk grafik
        val entries = sortedPrices.mapIndexed { index, price ->
            Entry(index.toFloat(), price.toFloat()) // Indeks bertambah ke kanan
        }

        val lineDataSet = LineDataSet(entries, "Predicted Prices").apply {
            color = android.graphics.Color.YELLOW // Warna garis
            valueTextSize = 12f // Ukuran teks nilai
            lineWidth = 2f // Ketebalan garis
            setCircleColor(android.graphics.Color.parseColor("#FFC100")) // Warna titik
            setDrawCircles(true) // Aktifkan titik pada garis
            setDrawFilled(true) // Mengaktifkan area yang terisi di bawah garis
            fillDrawable = resources.getDrawable(R.drawable.gradient_fill, null) // Drawable gradasi
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.apply {
            description.text = "Prediksi Harga Emas"
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f // Jarak antar nilai X
            xAxis.setDrawGridLines(false) // Hilangkan garis grid vertikal
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            axisLeft.valueFormatter = CurrencyValueFormatter() // Set formatter untuk sumbu Y
            animateX(1000)
            invalidate()
        }

        // Ambil titik harga terakhir
        val lastPrice = sortedPrices.lastOrNull()
        if (lastPrice != null) {
            findViewById<TextView>(R.id.titihargaterakhir).text =
                "${formatRupiah(lastPrice)}"
        }
    }

    private fun displayRecommendation(recommendation: String?) {
        if (recommendation == "WAIT") {
            val spannableText = SpannableString("""
            Ambil Langkah
            Tahan dulu untuk membeli emas, karena harganya sedang naik bulan ini. Namun, kamu tampaknya cocok untuk mencoba investasi saham. Yuk, cek peluang di pasar saham!
        """.trimIndent())

            // Membuat "Ambil Langkah" huruf tebal
            val boldText = "Ambil Langkah"
            val boldStart = spannableText.indexOf(boldText)
            val boldEnd = boldStart + boldText.length
            spannableText.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                boldStart,
                boldEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Membuat "Tahan dulu untuk membeli emas" huruf berwarna merah
            val redText = "Tahan dulu untuk membeli emas"
            val redStart = spannableText.indexOf(redText)
            val redEnd = redStart + redText.length
            spannableText.setSpan(
                android.text.style.ForegroundColorSpan(android.graphics.Color.RED),
                redStart,
                redEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Menampilkan teks yang telah diformat
            recommendationTextView.text = spannableText
        } else {
            recommendationTextView.text = recommendation ?: "Rekomendasi tidak tersedia"
        }
        // Tampilkan TextView setelah data tersedia
        recommendationTextView.visibility = View.VISIBLE
    }



    private fun showErrorMessage(message: String) {
        Log.e("EmasActivity", message)
    }
}
