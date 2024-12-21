package com.example.capstonesejahtera.namasaham

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.ApiService
import com.example.capstonesejahtera.CurrencyValueFormatter
import com.example.capstonesejahtera.JsonMember1DayPredictionDate
import com.example.capstonesejahtera.JsonMember1MonthPredictionDate
import com.example.capstonesejahtera.JsonMember1YearPredictionDate
import com.example.capstonesejahtera.R
import com.example.capstonesejahtera.SahamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

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

        call.enqueue(object : Callback<SahamResponse> {
            override fun onResponse(call: Call<SahamResponse>, response: Response<SahamResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val dayData = data.jsonMember1DayPredictionDate
                        val monthData = data.jsonMember1MonthPredictionDate
                        val yearData = data.jsonMember1YearPredictionDate

                        // Update tanggal prediksi
                        predictionDateTextView.text = dayData?.date ?: "Tanggal tidak tersedia"

                        // Konversi harga prediksi ke format persen
                        val dayPrice = formatToPercentage(dayData?.predictedPrice)
                        val monthPrice = formatToPercentage(monthData?.predictedPrice)
                        val yearPrice = formatToPercentage(yearData?.predictedPrice)

                        // Tampilkan harga prediksi
                        predictedPriceTextView.text = """
                            Prediksi Harian: $dayPrice
                            Prediksi Bulanan: $monthPrice
                            Prediksi Tahunan: $yearPrice
                        """.trimIndent()

                        // Tampilkan grafik
                        displayGraph(dayData, monthData, yearData)
                    } else {
                        predictionDateTextView.text = "Data tidak tersedia"
                        predictedPriceTextView.text = ""
                    }
                } else {
                    predictionDateTextView.text = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<SahamResponse>, t: Throwable) {
                predictionDateTextView.text = "Terjadi kesalahan: ${t.message}"
            }
        })
    }

    private fun formatToPercentage(value: Any?): String {
        return try {
            val number = value.toString().toDouble()
            String.format("%.2f%%", number * 100)
        } catch (e: Exception) {
            "Tidak tersedia"
        }
    }

    private fun displayGraph(dayData: JsonMember1DayPredictionDate?, monthData: JsonMember1MonthPredictionDate?, yearData: JsonMember1YearPredictionDate?) {
        val entries = mutableListOf<Entry>()

        dayData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(0f, price.toFloat()))
        }

        monthData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(1f, price.toFloat()))
        }

        yearData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(2f, price.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Predicted Prices").apply {
            color = android.graphics.Color.GREEN // Warna garis
            valueTextSize = 12f // Ukuran teks nilai
            lineWidth = 2f // Ketebalan garis
            setCircleColor(android.graphics.Color.parseColor("#FFC100")) // Warna titik
            setDrawCircles(true) // Aktifkan titik pada garis
            setDrawFilled(true) // Mengaktifkan area yang terisi di bawah garis
            fillDrawable = resources.getDrawable(R.drawable.line_chart_gradientt, null) // Drawable gradasi

            // Menambahkan ValueFormatter untuk menampilkan nilai sebagai persen
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry): String {
                    return formatToPercentage(entry.y)
                }
            }
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.apply {
            description.text = "Prediksi Saham"
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f // Jarak antar nilai X
            xAxis.setDrawGridLines(false) // Hilangkan garis grid vertikal
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            animateX(1000)
            invalidate()
        }
    }

}
