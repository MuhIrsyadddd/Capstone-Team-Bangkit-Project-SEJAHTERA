package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoSembilanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoSembilan : AppCompatActivity() {
    private lateinit var binding: ActivityResikoSembilanBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoSembilanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikosembilan) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet91.setOnClickListener {
            saveChoice("Tidak nyaman sama sekali ")
            highlightCircle(binding.bulet91)
        }
        binding.bulet92.setOnClickListener {
            saveChoice("Cukup nyaman")
            highlightCircle(binding.bulet92)
        }
        binding.bulet93.setOnClickListener {
            saveChoice("Sangat nyaman")
            highlightCircle(binding.bulet93)
        }
        binding.dasboard.setOnClickListener {
            navigateToResikodelapan()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet91.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet92.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet93.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(nyamanfluktuasi: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("nyamanfluktuasi" to nyamanfluktuasi) // Menggunakan mapOf

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
        val intent = Intent(this, DashboardActivity::class.java) // Ubah kelas target
        startActivity(intent)
    }
}