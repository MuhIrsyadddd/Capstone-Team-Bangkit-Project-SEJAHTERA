package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoEmpatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoEmpat : AppCompatActivity() {
    private lateinit var binding: ActivityResikoEmpatBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoEmpatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikoempat) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet41.setOnClickListener {
            saveChoice("<Tabungan masa depan")
            highlightCircle(binding.bulet41)
        }
        binding.bulet42.setOnClickListener {
            saveChoice("<Pendapatan pasif")
            highlightCircle(binding.bulet42)
        }
        binding.bulet43.setOnClickListener {
            saveChoice("<Spekulasi jangka pendek")
            highlightCircle(binding.bulet43)
        }
        binding.bulet44.setOnClickListener {
            saveChoice("<Persiapan pensiun")
            highlightCircle(binding.bulet44)
        }
        binding.selanjutnyalima.setOnClickListener {
            navigateToResikoLima()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet41.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet42.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet43.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet44.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(tujuaninvestasi: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("tujuaninvestasi" to tujuaninvestasi) // Menggunakan mapOf

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

    private fun navigateToResikoLima() {
        val intent = Intent(this, ResikoLima::class.java) // Ubah kelas target ke ResikoEmpat
        startActivity(intent)
    }
}
