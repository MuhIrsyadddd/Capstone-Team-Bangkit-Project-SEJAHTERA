package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.databinding.ActivityResikoTujuhBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ResikoTujuh : AppCompatActivity() {
    private lateinit var binding: ActivityResikoTujuhBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResikoTujuhBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resikotujuh) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.bulet71.setOnClickListener {
            saveChoice("Menjual untuk ambil keuntungan")
            highlightCircle(binding.bulet71)
        }
        binding.bulet72.setOnClickListener {
            saveChoice("Menunggu dan memantau")
            highlightCircle(binding.bulet72)
        }
        binding.bulet73.setOnClickListener {
            saveChoice("Membeli lebih banyak")
            highlightCircle(binding.bulet73)
        }
        binding.bulet74.setOnClickListener {
            saveChoice("Tidak yakin")
            highlightCircle(binding.bulet74)
        }
        binding.selanjutnyadelapan.setOnClickListener {
            navigateToResikodelapan()
        }
    }

    private fun highlightCircle(selectedCircle: ImageView) {
        // Reset semua lingkaran ke warna default
        binding.bulet71.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet72.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet73.setBackgroundResource(R.drawable.image_rectangle_normal)
        binding.bulet74.setBackgroundResource(R.drawable.image_rectangle_normal)

        // Set warna baru untuk lingkaran yang dipilih
        selectedCircle.setBackgroundResource(R.drawable.image_rectangle_selected)
    }

    private fun saveChoice(reaksikenaikan20persen: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("reaksikenaikan20persen" to reaksikenaikan20persen) // Menggunakan mapOf

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
        val intent = Intent(this, ResikoDelapan::class.java) // Ubah kelas target
        startActivity(intent)
    }
}