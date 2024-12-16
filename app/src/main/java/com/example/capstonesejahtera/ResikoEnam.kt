package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoEnamBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoEnam : AppCompatActivity() {
    private lateinit var binding: ActivityResikoEnamBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoEnamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikoenam) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet61.setOnClickListener {
            saveChoice("Menjual semuanya")
            highlightCircle(binding.bulet61)
        }
        binding.bulet62.setOnClickListener {
            saveChoice("Menunggu dan memantau")
            highlightCircle(binding.bulet62)
        }
        binding.bulet63.setOnClickListener {
            saveChoice("Membeli lebih banyak")
            highlightCircle(binding.bulet63)
        }
        binding.bulet64.setOnClickListener {
            saveChoice("<Tidak Yakin")
            highlightCircle(binding.bulet64)
        }
        binding.selanjutnyatujuh.setOnClickListener {
            navigateToResikoTujuh()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet61.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet62.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet63.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet64.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(reaksipenurunan20persen: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("reaksipenurunan20persen" to reaksipenurunan20persen) // Menggunakan mapOf

            // Update dokumen berdasarkan UID
            firestore.collection("resikoinvest").document(uid)
                .set(data, SetOptions.merge()) // Merge untuk menambahkan atau memperbarui data
                .addOnSuccessListener {
                    Toast.makeText(this, "Pilihan berhasil disimpan", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan pilihan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User belum login.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToResikoTujuh() {
        val intent = Intent(this, ResikoTujuh::class.java) // Ubah kelas target
        startActivity(intent)
    }
}