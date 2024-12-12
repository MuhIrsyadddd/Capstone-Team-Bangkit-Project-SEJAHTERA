package com.example.capstonesejahtera

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmasActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emas)

        // Inisialisasi Firestore
        firestore = Firebase.firestore

        // Inisialisasi TextView untuk menampilkan total
        totalTextView = findViewById(R.id.totalTextView)

        // Ambil data dan total
        fetchTabunganData()

        // Atur padding untuk window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchTabunganData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Ambil data dari Firestore
                    val userTabungan = firestore.collection("Tabungan").document(userId).collection("user_data")
                    val documents = userTabungan.get().await()

                    var totalNominal = 0.0

                    for (document in documents) {
                        val nominal = document.getDouble("nominal") ?: 0.0
                        totalNominal += nominal
                    }

                    // Tampilkan total di UI
                    launch(Dispatchers.Main) {
                        displayTotalNominal(totalNominal)
                    }
                } catch (e: Exception) {
                    Log.e("EmasActivity", "Error fetching tabungan data: ", e)
                    // Tangani kesalahan jika diperlukan
                }
            }
        } else {
            Log.w("EmasActivity", "User is not authenticated.")
        }
    }

    private fun displayTotalNominal(total: Double) {
        totalTextView.text = "Total Tabungan: $total"
    }
}
