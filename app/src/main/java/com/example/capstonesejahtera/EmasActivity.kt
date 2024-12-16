package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
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

class EmasActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var totalTextView: TextView
    private lateinit var totalCatatanTextView: TextView
    private lateinit var lineChart: LineChart

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emas)

        firestore = Firebase.firestore
        totalTextView = findViewById(R.id.tabungantotalemas)
        totalCatatanTextView = findViewById(R.id.totalcatetanemas)
        lineChart = findViewById(R.id.lineChart)

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

    private fun displayTotalNominal(total: Double) {
        val formattedTotal = formatCurrency(total)
        totalTextView.text = "Total Tabungan: $formattedTotal"
    }

    private fun displayTotalCatatanNominal(total: Double) {
        val formattedTotal = formatCurrency(total)
        totalCatatanTextView.text = "Total Catatan: $formattedTotal"
    }

    private fun formatCurrency(value: Double): String {
        val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
        format.minimumFractionDigits = 0
        format.maximumFractionDigits = 2
        return format.format(value)
    }

    private fun displayPredictedPrices(predictedPrices: List<Any?>?) {
        if (predictedPrices.isNullOrEmpty()) {
            showErrorMessage("Tidak ada data untuk ditampilkan.")
            return
        }

        val entries = predictedPrices.mapIndexed { index, price ->
            Entry(index.toFloat(), (price as? Number)?.toFloat() ?: 0f)
        }

        val lineDataSet = LineDataSet(entries, "Harga Prediksi")
        lineDataSet.color = resources.getColor(R.color.purple_200, null)
        lineDataSet.valueTextColor = resources.getColor(R.color.black, null)

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate() // Refresh grafik
    }

    private fun showErrorMessage(message: String) {
        Log.e("EmasActivity", message)
    }
}
