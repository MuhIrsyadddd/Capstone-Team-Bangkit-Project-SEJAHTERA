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
import com.example.capstonesejahtera.SahamResponse
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
import com.github.mikephil.charting.formatter.ValueFormatter

class MbmaSaham : AppCompatActivity() {
    private lateinit var predictionDateTextView: TextView
    private lateinit var predictedPriceTextView: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mbma_saham)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        predictionDateTextView = findViewById(R.id.prediction_date)
        predictedPriceTextView = findViewById(R.id.predicted_price)
        lineChart = findViewById(R.id.line_chart)

        fetchMbmaStockData()
    }

    private fun fetchMbmaStockData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://emasdansaham-810319962197.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getMbmaStockData()

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

        // Tambahkan data untuk 1 hari
        dayData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(0f, price.toFloat())) // Mengonversi Double ke Float
        }

        // Tambahkan data untuk 1 bulan
        monthData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(1f, price.toFloat())) // Mengonversi Double ke Float
        }

        // Tambahkan data untuk 1 tahun
        yearData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(2f, price.toFloat())) // Mengonversi Double ke Float
        }

        // Buat dataset untuk grafik
        val lineDataSet = LineDataSet(entries, "Harga Prediksi")
        lineDataSet.setDrawValues(true)
        lineDataSet.valueTextSize = 12f

        // Ubah nilai yang ditampilkan menjadi persen
        lineDataSet.valueFormatter = object : ValueFormatter() {
            override fun getPointLabel(entry: Entry): String {
                return formatToPercentage(entry.y) // Menggunakan fungsi formatToPercentage
            }
        }

        // Tambahkan warna berbeda untuk setiap titik
        lineDataSet.colors = listOf(
            resources.getColor(R.color.red, theme),  // Warna untuk 1 hari
            resources.getColor(R.color.blue, theme),    // Warna untuk 1 bulan
            resources.getColor(R.color.utama, theme) // Warna untuk 1 tahun
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
                LegendEntry("1 Hari", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.red, theme)),
                LegendEntry("1 Bulan", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.blue, theme)),
                LegendEntry("1 Tahun", Legend.LegendForm.CIRCLE, 10f, 2f, null, resources.getColor(R.color.utama, theme))
            )
        )

        // Refresh grafik
        lineChart.invalidate()
    }


}
