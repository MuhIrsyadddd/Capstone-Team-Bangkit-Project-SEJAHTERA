package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var tvName: TextView
    private lateinit var tvBirthDate: TextView
    private lateinit var tvGender: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inisialisasi TextView
        tvName = view.findViewById(R.id.tv_name)
        tvBirthDate = view.findViewById(R.id.tv_birth_date)
        tvGender = view.findViewById(R.id.tv_gender)

        fetchUserData()

        return view
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "Tidak ditemukan"
                        val birthDate = document.getString("birthDate") ?: "Tidak ditemukan"
                        val gender = document.getString("gender") ?: "Tidak ditemukan"

                        // Tampilkan data ke TextView
                        tvName.text = "Nama: $name"
                        tvBirthDate.text = "Tanggal Lahir: $birthDate"
                        tvGender.text = "Gender: $gender"
                    } else {
                        Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Gagal mengambil data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User tidak terautentikasi", Toast.LENGTH_SHORT).show()
        }
    }
}
