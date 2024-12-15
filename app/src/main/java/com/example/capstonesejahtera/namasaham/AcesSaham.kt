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
        val lineDataSet = LineDataSet(entries, "Predicted Prices")
        lineDataSet.color = resources.getColor(R.color.teal_700, theme)
        lineDataSet.valueTextSize = 12f

        // Buat LineData dan set ke chart
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate() // Refresh grafik
    }
}
