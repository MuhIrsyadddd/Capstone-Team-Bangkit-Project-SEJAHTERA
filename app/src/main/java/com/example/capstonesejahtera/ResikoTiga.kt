package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoTigaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoTiga : AppCompatActivity() {
    private lateinit var binding: ActivityResikoTigaBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoTigaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikotiga) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.containerRectangle3.setOnClickListener {
            saveChoice("<10%")
            highlightCircle(binding.containerRectangle3)
        }
        binding.containerRectangle2.setOnClickListener {
            saveChoice("10-20%")
            highlightCircle(binding.containerRectangle2)
        }
        binding.containerRectangle4.setOnClickListener {
            saveChoice("20-50%")
            highlightCircle(binding.containerRectangle4)
        }
        binding.containerRectangle7.setOnClickListener {
            saveChoice(">50%")
            highlightCircle(binding.containerRectangle7)
        }
        binding.selanjutnyaempat.setOnClickListener {
            navigateToResikoEmpat()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.containerRectangle3.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.containerRectangle2.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.containerRectangle4.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.containerRectangle7.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(persenpenghasilan: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("persenpenghasilan" to persenpenghasilan) // Menggunakan mapOf

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

    private fun navigateToResikoEmpat() {
        val intent = Intent(this, ResikoEmpat::class.java) // Ubah kelas target ke ResikoEmpat
        startActivity(intent)
    }
}
