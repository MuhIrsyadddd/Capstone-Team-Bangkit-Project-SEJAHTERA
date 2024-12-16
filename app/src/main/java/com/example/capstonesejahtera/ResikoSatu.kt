package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.example.capstonesejahtera.databinding.ActivityResikoSatuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions

class ResikoSatu : AppCompatActivity() {
    private lateinit var binding: ActivityResikoSatuBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoSatuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikosatu) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet11.setOnClickListener {
            saveChoice("<25 tahun")
            highlightCircle(binding.bulet11)
        }
        binding.bulet12.setOnClickListener {
            saveChoice("25-35 tahun")
            highlightCircle(binding.bulet12)
        }
        binding.bulet13.setOnClickListener {
            saveChoice("36-50 tahun")
            highlightCircle(binding.bulet13)
        }
        binding.bulet14.setOnClickListener {
            saveChoice(">50 tahun")
            highlightCircle(binding.bulet14)
        }
        binding.selanjutnyadua.setOnClickListener {
            navigateToResikodelapan()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet11.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet12.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet13.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet14.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(usia: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("usia" to usia) // Menggunakan mapOf

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

    private fun navigateToResikodelapan() {
        val intent = Intent(this, ResikoDua::class.java) // Ubah kelas target
        startActivity(intent)
    }
}