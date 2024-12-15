package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private var newsList: List<NewsItem> = listOf()
    private lateinit var greetingTextView: TextView  // Tambahkan ini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        greetingTextView = view.findViewById(R.id.greetingTextView)  // Inisialisasi greetingTextView
        updateGreetingMessage()  // Panggil fungsi untuk memperbarui pesan salam

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsAdapter = NewsAdapter(newsList)
        recyclerView.adapter = newsAdapter

        fetchNews()

        return view
    }

    private fun updateGreetingMessage() {
        val hourOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greeting = when {
            hourOfDay in 0..11 -> "Selamat Pagi"
            hourOfDay in 12..17 -> "Selamat Siang"
            else -> "Selamat Malam"
        }
        greetingTextView.text = greeting  // Set teks pada greetingTextView
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
                        // Filter articles dengan urlToImage yang tidak null atau tidak kosong
                        newsList = newsResponse.articles.filter { article ->
                            !article.urlToImage.isNullOrEmpty()  // Memeriksa apakah urlToImage null atau kosong
                        }
                        // Perbarui adapter hanya jika ada berita yang valid
                        if (newsList.isNotEmpty()) {
                            newsAdapter = NewsAdapter(newsList)
                            recyclerView.adapter = newsAdapter
                        } else {
                            // Tampilkan pesan atau lakukan tindakan jika tidak ada artikel yang valid
                        }
                    }
                } else {
                    // Tangani respons yang tidak berhasil
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Tangani kesalahan jaringan
            }
        })
    }


}
