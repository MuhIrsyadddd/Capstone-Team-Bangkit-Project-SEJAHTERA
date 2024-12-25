package com.example.capstonesejahtera

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HalamanLiatTabungan : AppCompatActivity() {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_halaman_liat_tabungan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            fetchTabunganData(userId)
        } else {
            Log.e("HalamanLiatTabungan", "User not authenticated")
        }
    }

    private fun fetchTabunganData(userId: String) {
        val dataLinearLayout: LinearLayout = findViewById(R.id.dataLinearLayout)

        firestore.collection("Tabungan")
            .document(userId)
            .collection("user_data")
            .get()
            .addOnSuccessListener { documents ->
                dataLinearLayout.removeAllViews() // Hapus semua view sebelumnya
                for (document in documents) {
                    val documentId = document.id // Ambil ID dokumen untuk referensi penghapusan
                    val nama = document.getString("nama")
                    val nominal = document.getDouble("nominal")

                    // Konversi nominal menjadi format angka tanpa pemisah
                    val formattedNominal = nominal?.let { formatToPlainNumber(it) }

                    // Buat LinearLayout untuk setiap item
                    val itemLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        setPadding(0, 8, 0, 8) // Tambahkan padding jika perlu
                    }

                    // Buat ImageView untuk menghapus
                    val deleteImageView = ImageView(this).apply {
                        setImageResource(R.drawable.baseline_auto_delete_24) // Set drawable
                        layoutParams = LinearLayout.LayoutParams(
                            48, // Width
                            48  // Height
                        ).apply {
                            setMargins(0, 0, 8, 0) // Margin antara image dan text
                        }

                        // Set listener untuk menghapus item saat diklik
                        setOnClickListener {
                            deleteTabunganData(userId, documentId)
                            dataLinearLayout.removeView(itemLayout) // Hapus item dari tampilan
                        }
                    }

                    // Buat TextView untuk data
                    val textView = TextView(this).apply {
                        text = "Nama: $nama\nNominal: $formattedNominal"
                        textSize = 16f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    // Tambahkan ImageView dan TextView ke itemLayout
                    itemLayout.addView(deleteImageView)
                    itemLayout.addView(textView)

                    // Tambahkan itemLayout ke LinearLayout utama
                    dataLinearLayout.addView(itemLayout)
                }
            }
            .addOnFailureListener { e ->
                Log.e("HalamanLiatTabungan", "Error fetching data", e)
            }
    }

    private fun deleteTabunganData(userId: String, documentId: String) {
        firestore.collection("Tabungan")
            .document(userId)
            .collection("user_data")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d("HalamanLiatTabungan", "Document $documentId successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.e("HalamanLiatTabungan", "Error deleting document", e)
            }
    }

    private fun formatToPlainNumber(amount: Double): String {
        return amount.toLong().toString() // Mengubah double ke long dan mengonversi ke string
    }
}
