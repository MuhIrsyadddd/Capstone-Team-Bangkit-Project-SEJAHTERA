package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        summaryAdapter = SummaryAdapter(summaryList)
        menuRecyclerView.adapter = summaryAdapter

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
