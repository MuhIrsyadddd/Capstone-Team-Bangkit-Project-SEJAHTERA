package com.example.capstonesejahtera.namasaham

import android.graphics.Color
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.view.View

class IncoSaham : AppCompatActivity() {
    private lateinit var predictionDateTextView: TextView
    private lateinit var predictedPriceTextView: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inco_saham)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        predictionDateTextView = findViewById(R.id.prediction_date)
        predictedPriceTextView = findViewById(R.id.predicted_price)
        lineChart = findViewById(R.id.line_chart)

        // Menyembunyikan TextView
        predictionDateTextView.visibility = View.GONE
        predictedPriceTextView.visibility = View.GONE

        fetchIncoStockData()

        // Set click listeners for buttons
        findViewById<TextView>(R.id.btn_1_day).setOnClickListener {
            val price = displayGraph(dayData, monthData, yearData, "day")
            findViewById<TextView>(R.id.titikhargaterakhirsaham).text = price?.let { formatToRupiah(it) } ?: "Tidak tersedia"
        }

        findViewById<TextView>(R.id.btn_1_month).setOnClickListener {
            val price = displayGraph(dayData, monthData, yearData, "month")
            findViewById<TextView>(R.id.titikhargaterakhirsaham).text = price?.let { formatToRupiah(it) } ?: "Tidak tersedia"
        }

        findViewById<TextView>(R.id.btn_1_year).setOnClickListener {
            val price = displayGraph(dayData, monthData, yearData, "year")
            findViewById<TextView>(R.id.titikhargaterakhirsaham).text = price?.let { formatToRupiah(it) } ?: "Tidak tersedia"
        }

    }

    private var dayData: JsonMember1DayPredictionDate? = null
    private var monthData: JsonMember1MonthPredictionDate? = null
    private var yearData: JsonMember1YearPredictionDate? = null

    private fun fetchIncoStockData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://emasdansaham-810319962197.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getIncoStockData()

        call.enqueue(object : Callback<SahamResponse> {
            override fun onResponse(call: Call<SahamResponse>, response: Response<SahamResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        dayData = data.jsonMember1DayPredictionDate
                        monthData = data.jsonMember1MonthPredictionDate
                        yearData = data.jsonMember1YearPredictionDate

                        predictionDateTextView.text = dayData?.date ?: "Tanggal tidak tersedia"

                        val dayPrice = formatToRupiah(dayData?.predictedPrice)
                        val monthPrice = formatToRupiah(monthData?.predictedPrice)
                        val yearPrice = formatToRupiah(yearData?.predictedPrice)

                        predictedPriceTextView.text = """
                            Prediksi Harian: $dayPrice
                            Prediksi Bulanan: $monthPrice
                            Prediksi Tahunan: $yearPrice
                        """.trimIndent()

                        // Tampilkan grafik dengan data awal (1 hari)
                        displayGraph(dayData, monthData, yearData, "day")
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

    private fun formatToRupiah(value: Any?): String {
        return try {
            val number = value.toString().toDouble()
            val rupiah = number * 15_000
            // Format dengan dua desimal dan pemisah ribuan
            String.format("Rp%,.2f", rupiah).replace(".", ",")
        } catch (e: Exception) {
            "Tidak tersedia"
        }
    }


    private fun displayGraph(
        dayData: JsonMember1DayPredictionDate?,
        monthData: JsonMember1MonthPredictionDate?,
        yearData: JsonMember1YearPredictionDate?,
        selectedPeriod: String
    ): Double? { // Mengubah tipe kembalian menjadi Double?
        val entries = mutableListOf<Entry>()
        val circleColors = mutableListOf<Int>()
        var selectedPrice: Double? = null // Variabel untuk menyimpan harga yang dipilih

        dayData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(0f, price.toFloat() * 15_000)) // Kalikan dengan 15.000
            circleColors.add(if (selectedPeriod == "day") Color.GREEN else Color.GRAY)
            if (selectedPeriod == "day") selectedPrice = price
        }

        monthData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(1f, price.toFloat() * 15_000)) // Kalikan dengan 15.000
            circleColors.add(if (selectedPeriod == "month") Color.BLUE else Color.GRAY)
            if (selectedPeriod == "month") selectedPrice = price
        }

        yearData?.let {
            val price = it.predictedPrice.toString().toDoubleOrNull() ?: 0.0
            entries.add(Entry(2f, price.toFloat() * 15_000)) // Kalikan dengan 15.000
            circleColors.add(if (selectedPeriod == "year") Color.MAGENTA else Color.GRAY)
            if (selectedPeriod == "year") selectedPrice = price
        }

        val dataSet = LineDataSet(entries, "Prediksi Saham").apply {
            color = Color.GREEN // Warna garis
            valueTextSize = 12f
            lineWidth = 2f
            setDrawCircles(true)
            setCircleColors(circleColors) // Warna titik disesuaikan
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawFilled(true) // Mengaktifkan pengisian di bawah garis
            fillDrawable = resources.getDrawable(R.drawable.line_chart_gradientt) // Mengatur gradien sebagai pengisian
            valueFormatter = CurrencyValueFormatter() // Gunakan formatter
        }

        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.apply {
            description.text = "Prediksi Saham"
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            animateX(1000)
            invalidate()
        }

        return selectedPrice // Kembalikan harga yang dipilih
    }

}
