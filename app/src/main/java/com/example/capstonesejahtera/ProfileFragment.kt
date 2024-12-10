package com.example.capstonesejahtera

import android.content.Intent
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
    private lateinit var tvEmail: TextView
    private lateinit var tvBirthDate: TextView
    private lateinit var tvGender: TextView
    private lateinit var textLogOut: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inisialisasi TextView
        tvName = view.findViewById(R.id.text_naufal)
        tvEmail = view.findViewById(R.id.text_email_naufal)
        tvBirthDate = view.findViewById(R.id.text_birthdate)
        tvGender = view.findViewById(R.id.text_gender)
        textLogOut = view.findViewById(R.id.text_log_out)

        // Ambil data pengguna
        fetchUserData()

        // Tambahkan logika klik untuk Log Out
        textLogOut.setOnClickListener {
            logOut()
        }

        return view
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid
        val email = auth.currentUser?.email

        if (uid != null) {
            // Ambil data pengguna dari Firestore
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "Tidak ditemukan"
                        val birthDate = document.getString("birthDate") ?: "Tidak ditemukan"
                        val gender = document.getString("gender") ?: "Tidak ditemukan"

                        // Tampilkan data ke TextView
                        tvName.text = name
                        tvEmail.text = email ?: "Tidak ditemukan"
                        tvBirthDate.text = birthDate
                        tvGender.text = gender
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

    private fun logOut() {
        auth.signOut() // Log out dari Firebase
        Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()
        // Arahkan ke MainActivity
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
