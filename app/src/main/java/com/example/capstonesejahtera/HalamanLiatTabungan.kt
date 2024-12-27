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
import java.text.NumberFormat
import java.util.Locale

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
                    val maksimal = document.getDouble("maksimal") // Pastikan Anda juga mendapatkan data maksimal jika diperlukan

                    // Konversi nominal menjadi format angka dengan titik
                    val formattedNominal = nominal?.let { formatToPlainNumber(it) }

                    // Inflate layout item_tabungan.xml
                    val itemView = layoutInflater.inflate(R.layout.item_tabungan, dataLinearLayout, false)

                    // Mengatur nama dan nominal di TextView
                    val namaTextView: TextView = itemView.findViewById(R.id.nama_tabungan)
                    val nominalTextView: TextView = itemView.findViewById(R.id.nominal_tabungan)
                    namaTextView.text = nama ?: "Nama tidak tersedia"
                    nominalTextView.text = formattedNominal ?: "Rp0"

                    // Menambahkan listener untuk item agar dapat membuka PopUpProgressTabungan
                    itemView.setOnClickListener {
                        val popUp = PopUpProgressTabungan()
                        val bundle = Bundle().apply {
                            putString("NAMA", nama)
                            putLong("NOMINAL", nominal?.toLong() ?: 0)
                            putLong("MAKSIMAL", maksimal?.toLong() ?: 0) // Pastikan untuk mengirim data maksimal
                        }
                        popUp.arguments = bundle
                        popUp.show(supportFragmentManager, "PopUpProgressTabungan")
                    }

                    // Mengatur listener untuk menghapus item saat diklik
                    val deleteImageView: ImageView = itemView.findViewById(R.id.icon_hapuss)
                    deleteImageView.setOnClickListener {
                        deleteTabunganData(userId, documentId)
                        dataLinearLayout.removeView(itemView) // Hapus item dari tampilan
                    }

                    // Tambahkan itemView ke LinearLayout utama
                    dataLinearLayout.addView(itemView)
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
        val numberFormat = NumberFormat.getInstance(Locale("id", "ID")) // Format angka sesuai locale Indonesia
        return numberFormat.format(amount.toLong()) // Mengubah double ke long dan mengonversi ke string
    }
}
