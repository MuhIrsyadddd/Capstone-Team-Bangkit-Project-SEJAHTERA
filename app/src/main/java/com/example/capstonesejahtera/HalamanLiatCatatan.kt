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

class HalamanLiatCatatan : AppCompatActivity() {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_halaman_liat_catatan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            fetchUserData(userId)
        } else {
            Log.e("HalamanLiatCatatan", "User not authenticated")
        }
    }

    private fun fetchUserData(userId: String) {
        val dataTextView: TextView = findViewById(R.id.dataTextView)

        firestore.collection("Catatan")
            .document(userId)
            .collection("user_data")
            .get()
            .addOnSuccessListener { documents ->
                val dataBuilder = StringBuilder()
                for (document in documents) {
                    val nama = document.getString("nama")
                    val nominal = document.getDouble("nominal")

                    // Konversi nominal menjadi format angka tanpa pemisah
                    val formattedNominal = nominal?.let { formatToPlainNumber(it) }

                    dataBuilder.append("Nama: $nama\nNominal: $formattedNominal\n\n")
                }
                dataTextView.text = dataBuilder.toString()
            }
            .addOnFailureListener { e ->
                Log.e("HalamanLiatCatatan", "Error fetching data", e)
            }
    }

    private fun formatToPlainNumber(amount: Double): String {
        return amount.toLong().toString() // Mengubah double ke long dan mengonversi ke string
    }
}
