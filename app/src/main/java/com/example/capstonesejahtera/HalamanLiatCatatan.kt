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
import java.text.DecimalFormat

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
        val dataLinearLayout: LinearLayout = findViewById(R.id.dataLinearLayout)

        firestore.collection("Catatan")
            .document(userId)
            .collection("user_data")
            .get()
            .addOnSuccessListener { documents ->
                dataLinearLayout.removeAllViews() // Hapus semua view sebelumnya
                for (document in documents) {
                    val documentId = document.id // Ambil ID dokumen untuk referensi penghapusan
                    val nama = document.getString("nama")
                    val nominal = document.getDouble("nominal")

                    // Konversi nominal menjadi format angka dengan pemisah ribuan
                    val formattedNominal = nominal?.let { formatToPlainNumber(it) }

                    // Inflate layout item_catatan
                    val itemView = layoutInflater.inflate(R.layout.item_catatan, dataLinearLayout, false)

                    // Temukan dan atur nilai di dalam itemView
                    val namaTextView = itemView.findViewById<TextView>(R.id.nama_catatan)
                    val nominalTextView = itemView.findViewById<TextView>(R.id.nominal_pengeluaran)
                    val deleteImageView = itemView.findViewById<ImageView>(R.id.icon_hapus)

                    namaTextView.text = nama ?: "Nama tidak tersedia"
                    nominalTextView.text = formattedNominal?.let { "Rp$it" } ?: "Rp0"

                    // Set listener untuk menghapus item saat diklik
                    deleteImageView.setOnClickListener {
                        deleteUserData(userId, documentId)
                        dataLinearLayout.removeView(itemView) // Hapus item dari tampilan
                    }

                    // Tambahkan listener untuk menampilkan PopUpRubahCatatan saat item diklik
                    itemView.setOnClickListener {
                        val popUpRubahCatatan = PopUpRubahCatatan().apply {
                            arguments = Bundle().apply {
                                putString("NAMA_CATATAN", nama)
                                putLong("NOMINAL_CATATAN", nominal?.toLong() ?: 0L)
                                putString("CATATAN_ID", documentId)
                            }
                        }
                        popUpRubahCatatan.show(supportFragmentManager, "PopUpRubahCatatan")
                    }

                    // Tambahkan itemView ke LinearLayout utama
                    dataLinearLayout.addView(itemView)
                }
            }
            .addOnFailureListener { e ->
                Log.e("HalamanLiatCatatan", "Error fetching data", e)
            }
    }



    private fun deleteUserData(userId: String, documentId: String) {
        firestore.collection("Catatan")
            .document(userId)
            .collection("user_data")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d("HalamanLiatCatatan", "Document $documentId successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.e("HalamanLiatCatatan", "Error deleting document", e)
            }
    }

    private fun formatToPlainNumber(amount: Double): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount.toLong()) // Mengubah double ke long dan memformat dengan pemisah ribuan
    }
}
