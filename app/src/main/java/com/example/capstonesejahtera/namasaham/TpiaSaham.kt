package com.example.capstonesejahtera.NamaSaham

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.ApiService
import com.example.capstonesejahtera.JsonMember1DayPredictionDate
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

class TpiaSaham : AppCompatActivity() {
    private lateinit var predictionDateTextView: TextView
    private lateinit var predictedPriceTextView: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tpia_saham)

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
                    val data = response.body()?.jsonMember1DayPredictionDate
                    if (data != null) {
                        predictionDateTextView.text = data.date ?: "Tanggal tidak tersedia"
                        predictedPriceTextView.text = data.predictedPrice?.toString() ?: "Harga tidak tersedia"

                        // Tampilkan data dalam grafik
                        displayGraph(data)
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

    private fun displayGraph(data: JsonMember1DayPredictionDate) {
        // Safely convert predictedPrice to Float
        val predictedPrice = when (val price = data.predictedPrice) {
            is Number -> price.toFloat() // Convert if it's a Number
            is String -> price.toFloatOrNull() ?: 0f // Convert if it's a String, fallback to 0f
            else -> 0f // Fallback if it's neither
        }

        // Use a list with explicit Float values
        val predictedPrices = listOf(
            100f,  // Dummy data
            110f,  // Dummy data
            105f,  // Dummy data
            120f,  // Dummy data
            predictedPrice // Use the safely converted predicted price
        )
        val entries = mutableListOf<Entry>()

        predictedPrices.forEachIndexed { index, price ->
            entries.add(Entry(index.toFloat(), price))
        }

        val lineDataSet = LineDataSet(entries, "Predicted Prices")
        lineDataSet.color = resources.getColor(R.color.teal_700, theme)
        lineDataSet.valueTextSize = 12f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate() // Refresh grafik
    }


}
