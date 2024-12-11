package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoDelapanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoDelapan : AppCompatActivity() {
    private lateinit var binding: ActivityResikoDelapanBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoDelapanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikodelapan) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet81.setOnClickListener {
            saveChoice("Belum pernah ")
            highlightCircle(binding.bulet81)
        }
        binding.bulet82.setOnClickListener {
            saveChoice("Pernah sekali ")
            highlightCircle(binding.bulet82)
        }
        binding.bulet83.setOnClickListener {
            saveChoice("Sering")
            highlightCircle(binding.bulet83)
        }
        binding.bulet84.setOnClickListener {
            saveChoice("Aktif berinvestasi")
            highlightCircle(binding.bulet84)
        }
        binding.selanjutnyasembilan.setOnClickListener {
            navigateToResikosembilan()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet81.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet82.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet83.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet84.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(pengalamaninvestasi: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("pengalamaninvestasi" to pengalamaninvestasi) // Menggunakan mapOf

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

    private fun navigateToResikosembilan() {
        val intent = Intent(this, ResikoSembilan::class.java) // Ubah kelas target
        startActivity(intent)
    }
}