package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoLimaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoLima : AppCompatActivity() {
    private lateinit var binding: ActivityResikoLimaBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoLimaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikolima) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet51.setOnClickListener {
            saveChoice("<1 tahun")
            highlightCircle(binding.bulet51)
        }
        binding.bulet52.setOnClickListener {
            saveChoice("1-3 tahun")
            highlightCircle(binding.bulet52)
        }
        binding.bulet53.setOnClickListener {
            saveChoice("3-5 tahun")
            highlightCircle(binding.bulet53)
        }
        binding.bulet54.setOnClickListener {
            saveChoice(">5 tahun")
            highlightCircle(binding.bulet54)
        }
        binding.selanjutnyaenam.setOnClickListener {
            navigateToResikoLima()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet51.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet52.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet53.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet54.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(durasiinvestasi: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("durasiinvestasi" to durasiinvestasi) // Menggunakan mapOf

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
        val intent = Intent(this, ResikoEnam::class.java) // Ubah kelas target ke ResikoEmpat
        startActivity(intent)
    }
}