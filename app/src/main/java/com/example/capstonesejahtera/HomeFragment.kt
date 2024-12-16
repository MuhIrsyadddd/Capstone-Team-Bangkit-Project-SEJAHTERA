package com.example.capstonesejahtera

import SummaryAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonesejahtera.namasaham.AcesSaham
import com.example.capstonesejahtera.namasaham.AdroSaham
import com.example.capstonesejahtera.namasaham.AkraSaham
import com.example.capstonesejahtera.namasaham.AmmnSaham
import com.example.capstonesejahtera.namasaham.AmrtSaham
import com.example.capstonesejahtera.namasaham.AntmSaham
import com.example.capstonesejahtera.namasaham.ArtoSaham
import com.example.capstonesejahtera.namasaham.AsiiSaham
import com.example.capstonesejahtera.namasaham.BbcaSaham
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private var newsList: List<NewsItem> = listOf()
    private lateinit var greetingTextView: TextView

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var summaryAdapter: SummaryAdapter
    private var summaryList: MutableList<SummaryItem> = mutableListOf()

    private lateinit var stockRecyclerView: RecyclerView
    private lateinit var stockAdapter: StockAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi greetingTextView
        greetingTextView = view.findViewById(R.id.greetingTextView)
        updateGreetingMessage()

        // Inisialisasi RecyclerView untuk berita
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsAdapter = NewsAdapter(newsList)
        recyclerView.adapter = newsAdapter

        // Inisialisasi RecyclerView untuk summary
        menuRecyclerView = view.findViewById(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Buat SummaryAdapter dengan listener
        summaryAdapter = SummaryAdapter(summaryList) { position ->
            // Handle click berdasarkan posisi
            when (position) {
                0 -> startActivity(Intent(context, HalamanCatatanKhusus::class.java)) // Catatan
                1 -> startActivity(Intent(context, HalamanTabunganKhusus::class.java)) // Tabungan
                // Tambahkan lebih banyak case jika perlu
            }
        }

        menuRecyclerView.adapter = summaryAdapter

        // Inisialisasi RecyclerView untuk saham
        stockRecyclerView = view.findViewById(R.id.stockRecyclerView)
        stockRecyclerView.layoutManager = LinearLayoutManager(context)
        val stocks = listOf(
            StockItem("1. ACES"),
            StockItem("2. ADRO"),
            StockItem("3. AKRA"),
            StockItem("4. AMMN"),
            StockItem("5. AMRT"),
            StockItem("6. ANTM"),
            StockItem("7. ARTO"),
            StockItem("8. ASII"),
            StockItem("9. BBCA"),
        )
        stockAdapter = StockAdapter(stocks) { stockName ->
            when (stockName) {
                "1. ACES" -> startActivity(Intent(context, AcesSaham::class.java))
                "2. ADRO" -> startActivity(Intent(context, AdroSaham::class.java))
                "3. AKRA" -> startActivity(Intent(context, AkraSaham::class.java))
                "4. AMMN" -> startActivity(Intent(context, AmmnSaham::class.java))
                "5. AMRT" -> startActivity(Intent(context, AmrtSaham::class.java))
                "6. ANTM" -> startActivity(Intent(context, AntmSaham::class.java))
                "7. ARTO" -> startActivity(Intent(context, ArtoSaham::class.java))
                "8. ASII" -> startActivity(Intent(context, AsiiSaham::class.java))
                "9. BBCA" -> startActivity(Intent(context, BbcaSaham::class.java))
            }
        }
        stockRecyclerView.adapter = stockAdapter

        // Panggil fungsi untuk mendapatkan data
        fetchNews()
        fetchSummaryData()

        return view
    }


    private fun updateGreetingMessage() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // Ambil nama dari Firestore
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "Pengguna"
                    val hourOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                    val greeting = when {
                        hourOfDay in 0..11 -> "Selamat Pagi. \n$name"
                        hourOfDay in 12..17 -> "Selamat Siang. \n$name"
                        else -> "Selamat Malam. \n$name"
                    }
                    greetingTextView.text = greeting
                } else {
                    greetingTextView.text = "Halo, Pengguna!"
                }
            }
            .addOnFailureListener {
                greetingTextView.text = "Halo, Pengguna!"
            }
    }

    private fun fetchNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://capstone-443407.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getBusinessNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        newsList = newsResponse.articles.filter { article ->
                            !article.urlToImage.isNullOrEmpty()
                        }
                        if (newsList.isNotEmpty()) {
                            newsAdapter = NewsAdapter(newsList)
                            recyclerView.adapter = newsAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Tangani kesalahan jaringan
            }
        })
    }

    private fun fetchSummaryData() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Ambil data Catatan
        db.collection("Catatan").document(userId).collection("user_data")
            .get()
            .addOnSuccessListener { documents ->
                var totalCatatan = 0L
                for (doc in documents) {
                    val nominal = doc.getLong("nominal") ?: 0L
                    totalCatatan += nominal
                }
                summaryList.add(SummaryItem("Total Catatan", totalCatatan))
                summaryAdapter.notifyDataSetChanged()
            }

        // Ambil data Tabungan
        db.collection("Tabungan").document(userId).collection("user_data")
            .get()
            .addOnSuccessListener { documents ->
                var totalTabungan = 0L
                for (doc in documents) {
                    val nominal = doc.getLong("nominal") ?: 0L
                    totalTabungan += nominal
                }
                summaryList.add(SummaryItem("Total Tabungan", totalTabungan))
                summaryAdapter.notifyDataSetChanged()
            }
    }
}
