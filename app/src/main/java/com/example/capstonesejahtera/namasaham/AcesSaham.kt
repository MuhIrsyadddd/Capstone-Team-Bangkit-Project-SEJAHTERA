package com.example.capstonesejahtera.namasaham

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.ApiService
import com.example.capstonesejahtera.JsonMember1DayPredictionDate
import com.example.capstonesejahtera.JsonMember1MonthPredictionDate
import com.example.capstonesejahtera.JsonMember1YearPredictionDate
import com.example.capstonesejahtera.R
import com.example.capstonesejahtera.SahamAcesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AcesSaham : AppCompatActivity() {
    private lateinit var predictionDateTextView: TextView
    private lateinit var predictedPriceTextView: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aces_saham)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        predictionDateTextView = findViewById(R.id.prediction_date)
        predictedPriceTextView = findViewById(R.id.predicted_price)
        lineChart = findViewById(R.id.line_chart)

        fetchAcesStockData()
    }

    private fun fetchAcesStockData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://emasdansaham-810319962197.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getAcesStockData()

        call.enqueue(object : Callback<SahamAcesResponse> {
            override fun onResponse(call: Call<SahamAcesResponse>, response: Response<SahamAcesResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // Ambil data dari setiap prediksi
                        val dayData = data.jsonMember1DayPredictionDate
                        val monthData = data.jsonMember1MonthPredictionDate
                        val yearData = data.jsonMember1YearPredictionDate

                        // Update tampilan tanggal
                        predictionDateTextView.text = dayData?.date ?: "Tanggal tidak tersedia"

                        // Ambil harga prediksi untuk 1 hari, 1 bulan, dan 1 tahun
                        val dayPrice = dayData?.predictedPrice?.toString() ?: "Harga tidak tersedia"
                        val monthPrice = monthData?.predictedPrice?.toString() ?: "Harga tidak tersedia"
                        val yearPrice = yearData?.predictedPrice?.toString() ?: "Harga tidak tersedia"

                        // Tampilkan semua harga prediksi dalam satu TextView
                        predictedPriceTextView.text = "Harga Prediksi (Harian): $dayPrice\n" +
                                "Harga Prediksi (Bulanan): $monthPrice\n" +
                                "Harga Prediksi (Tahunan): $yearPrice"

                        // Tampilkan data dalam grafik
                        displayGraph(dayData, monthData, yearData)
                    } else {
                        predictionDateTextView.text = "Data tidak tersedia"
                        predictedPriceTextView.text = ""
                    }
                } else {
                    predictionDateTextView.text = "Gagal memuat data"
                }
            }


            override fun onFailure(call: Call<SahamAcesResponse>, t: Throwable) {
                predictionDateTextView.text = "Terjadi kesalahan: ${t.message}"
            }
        })
    }

    private fun displayGraph(dayData: JsonMember1DayPredictionDate?, monthData: JsonMember1MonthPredictionDate?, yearData: JsonMember1YearPredictionDate?) {
        val entries = mutableListOf<Entry>()

        // Tambahkan data untuk 1 hari
        dayData?.let {
            val price = it.predictedPrice.toString().toFloatOrNull() ?: 0f
            entries.add(Entry(0f, price)) // Menggunakan index 0 untuk data harian
        }

        // Tambahkan data untuk 1 bulan
        monthData?.let {
            val price = it.predictedPrice.toString().toFloatOrNull() ?: 0f
            entries.add(Entry(1f, price)) // Menggunakan index 1 untuk data bulanan
        }

        // Tambahkan data untuk 1 tahun
        yearData?.let {
            val price = it.predictedPrice.toString().toFloatOrNull() ?: 0f
            entries.add(Entry(2f, price)) // Menggunakan index 2 untuk data tahunan
        }

        // Buat dataset untuk grafik
        val lineDataSet = LineDataSet(entries, "Harga Prediksi")
        lineDataSet.setDrawValues(true)
        lineDataSet.valueTextSize = 12f

        // Tambahkan warna berbeda untuk setiap titik
        lineDataSet.colors = listOf(
            resources.getColor(R.color.teal_700, theme),  // Warna untuk 1 hari
            resources.getColor(R.color.utama, theme),    // Warna untuk 1 bulan
            resources.getColor(R.color.purple_200, theme) // Warna untuk 1 tahun
        )

        // Tambahkan keterangan titik
        lineDataSet.setDrawIcons(false)
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleColors = lineDataSet.colors // Gunakan warna yang sama untuk lingkaran

        // Buat LineData dan set ke chart
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Tambahkan legenda untuk menjelaskan 3 titik
        val legend = lineChart.legend
        legend.isEnabled = true
        legend.textSize = 12f

// Menempatkan legenda di bawah grafik secara horizontal
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT

// Menambahkan jarak antar item legenda
        legend.formToTextSpace = 12f   // Jarak antara bentuk simbol dan teks
        legend.xEntrySpace = 40f       // Jarak horizontal antar item legenda

// Menambahkan warna dan label custom untuk legenda
        legend.setCustom(
            listOf(
                LegendEntry("1 Hari", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.teal_700, theme)),
                LegendEntry("1 Bulan", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.utama, theme)),
                LegendEntry("1 Tahun", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.purple_200, theme))
            )
        )


        // Refresh grafik
        lineChart.invalidate()
    }

}
