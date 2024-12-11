package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstonesejahtera.databinding.ActivityResikoDuaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions

class ResikoDua : AppCompatActivity() {
    private lateinit var binding: ActivityResikoDuaBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityResikoDuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        // Set onClickListener untuk mengubah warna lingkaran dan menyimpan data
        binding.containerRectangle3.setOnClickListener {
            highlightCircle(binding.containerRectangle3)
            saveResiko("<5 Juta")
        }
        binding.containerRectangle2.setOnClickListener {
            highlightCircle(binding.containerRectangle2)
            saveResiko("5 - 10 Juta")
        }
        binding.containerRectangle4.setOnClickListener {
            highlightCircle(binding.containerRectangle4)
            saveResiko("10 - 20 Juta")
        }
        binding.containerRectangle7.setOnClickListener {
            highlightCircle(binding.containerRectangle7)
            saveResiko(">20 Juta")
        }

        binding.selanjutnyatiga.setOnClickListener {
            navigateToResikoTiga()
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

    private fun saveResiko(penghasilan: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val data = mapOf("penghasilan" to penghasilan)

            // Update dokumen berdasarkan UID
            db.collection("resikoinvest").document(uid)
                .set(data, SetOptions.merge()) // Merge untuk menambahkan atau memperbarui data
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan data: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User belum login.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToResikoTiga() {
        val intent = Intent(this, ResikoTiga::class.java) // Ubah kelas target ke ResikoTiga
        startActivity(intent)
    }
}
