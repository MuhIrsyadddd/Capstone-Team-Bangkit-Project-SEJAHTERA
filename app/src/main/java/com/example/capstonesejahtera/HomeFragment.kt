package com.example.capstonesejahtera

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.example.capstonesejahtera.namasaham.BbniSaham
import com.example.capstonesejahtera.namasaham.BbriSaham
import com.example.capstonesejahtera.namasaham.BbtnSaham
import com.example.capstonesejahtera.namasaham.BmriSaham
import com.example.capstonesejahtera.namasaham.BrisSaham
import com.example.capstonesejahtera.namasaham.BrptSaham
import com.example.capstonesejahtera.namasaham.BukaSaham
import com.example.capstonesejahtera.namasaham.CpinSaham
import com.example.capstonesejahtera.namasaham.EmtkSaham
import com.example.capstonesejahtera.namasaham.EssaSaham
import com.example.capstonesejahtera.namasaham.ExclSaham
import com.example.capstonesejahtera.namasaham.GgrmSaham
import com.example.capstonesejahtera.namasaham.GotoSaham
import com.example.capstonesejahtera.namasaham.HrumSaham
import com.example.capstonesejahtera.namasaham.IcbpSaham
import com.example.capstonesejahtera.namasaham.IncoSaham
import com.example.capstonesejahtera.namasaham.IndfSaham
import com.example.capstonesejahtera.namasaham.IndySaham
import com.example.capstonesejahtera.namasaham.InkpSaham
import com.example.capstonesejahtera.namasaham.IntpSaham
import com.example.capstonesejahtera.namasaham.IsatSaham
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
        summaryAdapter = SummaryAdapter(summaryList) { summaryItem ->
            when (summaryItem.title) {
                "Total Catatan" -> startActivity(Intent(context, HalamanLiatCatatan::class.java))
                "Total Tabungan" -> startActivity(Intent(context, HalamanLiatTabungan::class.java))
                else -> {
                    // Handle fallback jika diperlukan
                }
            }
        }
        menuRecyclerView.adapter = summaryAdapter

        // Inisialisasi RecyclerView untuk saham
        stockRecyclerView = view.findViewById(R.id.stockRecyclerView)
        stockRecyclerView.layoutManager = LinearLayoutManager(context)

        val originalStocks = listOf(
            StockItem("1. ACES"),
            StockItem("2. ADRO"),
            StockItem("3. AKRA"),
            StockItem("4. AMMN"),
            StockItem("5. AMRT"),
            StockItem("6. ANTM"),
            StockItem("7. ARTO"),
            StockItem("8. ASII"),
            StockItem("9. BBCA"),
            StockItem("10. BBNI"),
            StockItem("11. BBRI"),
            StockItem("12. BBTN"),
            StockItem("13. BMRI"),
            StockItem("14. BRIS"),
            StockItem("15. BRPT"),
            StockItem("16. BUKA"),
            StockItem("17. CPIN"),
            StockItem("18. EMTK"),
            StockItem("19. ESSA"),
            StockItem("20. EXCL"),
            StockItem("21. GGRM"),
            StockItem("22. GOTO"),
            StockItem("23. HRUM"),
            StockItem("24. ICBP"),
            StockItem("25. INCO"),
            StockItem("26. INDF"),
            StockItem("27. INDY"),
            StockItem("28. INKP"),
            StockItem("29. INTP"),
            StockItem("30. ISAT")
        )
        val filteredStocks = originalStocks.toMutableList()

        stockAdapter = StockAdapter(filteredStocks) { stockName ->
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
                "10. BBNI" -> startActivity(Intent(context, BbniSaham::class.java))
                "11. BBRI" -> startActivity(Intent(context, BbriSaham::class.java))
                "12. BBTN" -> startActivity(Intent(context, BbtnSaham::class.java))
                "13. BMRI" -> startActivity(Intent(context, BmriSaham::class.java))
                "14. BRIS" -> startActivity(Intent(context, BrisSaham::class.java))
                "15. BRPT" -> startActivity(Intent(context, BrptSaham::class.java))
                "16. BUKA" -> startActivity(Intent(context, BukaSaham::class.java))
                "17. CPIN" -> startActivity(Intent(context, CpinSaham::class.java))
                "18. EMTK" -> startActivity(Intent(context, EmtkSaham::class.java))
                "19. ESSA" -> startActivity(Intent(context, EssaSaham::class.java))
                "20. EXCL" -> startActivity(Intent(context, ExclSaham::class.java))
                "21. GGRM" -> startActivity(Intent(context, GgrmSaham::class.java))
                "22. GOTO" -> startActivity(Intent(context, GotoSaham::class.java))
                "23. HRUM" -> startActivity(Intent(context, HrumSaham::class.java))
                "24. ICBP" -> startActivity(Intent(context, IcbpSaham::class.java))
                "25. INCO" -> startActivity(Intent(context, IncoSaham::class.java))
                "26. INDF" -> startActivity(Intent(context, IndfSaham::class.java))
                "27. INDY" -> startActivity(Intent(context, IndySaham::class.java))
                "28. INKP" -> startActivity(Intent(context, InkpSaham::class.java))
                "29. INTP" -> startActivity(Intent(context, IntpSaham::class.java))
                "30. ISAT" -> startActivity(Intent(context, IsatSaham::class.java))
            }
        }
        stockRecyclerView.adapter = stockAdapter

        // Inisialisasi pencarian saham
        val stockSearchEditText = view.findViewById<EditText>(R.id.searchEditText)
        stockSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                filteredStocks.clear()
                if (query.isEmpty()) {
                    // Jika query kosong, tampilkan semua saham
                    filteredStocks.addAll(originalStocks)
                } else {
                    // Filter saham berdasarkan query
                    val results = originalStocks.filter { it.name.lowercase().contains(query) }
                    if (results.isEmpty()) {
                        // Jika tidak ada hasil, tampilkan semua saham
                        filteredStocks.addAll(originalStocks)
                    } else {
                        filteredStocks.addAll(results)
                    }
                }
                stockAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Inisialisasi pencarian berita
        val newsSearchEditText = view.findViewById<EditText>(R.id.searchEditText)
        newsSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSearchResults(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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
                    val spannableGreeting = SpannableString(greeting)
                    val startIndex = greeting.indexOf(name)
                    val endIndex = startIndex + name.length
                    spannableGreeting.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    greetingTextView.text = spannableGreeting
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
                        // Filter artikel dengan urlToImage yang tidak kosong
                        newsList = newsResponse.articles.filter { article ->
                            !article.urlToImage.isNullOrEmpty()
                        }

                        // Update RecyclerView dengan semua data
                        if (newsList.isNotEmpty()) {
                            newsAdapter = NewsAdapter(newsList)
                            recyclerView.adapter = newsAdapter
                        }

                        // Update hasil pencarian dengan query kosong untuk menampilkan semua data
                        updateSearchResults("")
                    }
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Tangani kesalahan jaringan
            }
        })
    }

    private fun updateSearchResults(query: String) {
        val filteredNews = if (query.isBlank()) {
            newsList // Tampilkan semua berita jika query kosong
        } else {
            newsList.filter { it.title.contains(query, ignoreCase = true) }
        }

        // Periksa apakah hasil pencarian kosong
        if (filteredNews.isEmpty()) {
            newsAdapter.updateNews(newsList) // Tampilkan semua berita jika tidak ada hasil
        } else {
            newsAdapter.updateNews(filteredNews) // Tampilkan hasil pencarian
        }
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
