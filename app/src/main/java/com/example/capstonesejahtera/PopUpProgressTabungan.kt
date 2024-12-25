package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PopUpProgressTabungan : DialogFragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_pop_up_progress_tabungan, container, false)

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.popupupdate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        arguments?.let {
            val nama = it.getString("NAMA", "Tidak ada nama")
            val nominal = it.getLong("NOMINAL", 0)
            val maksimal = it.getLong("MAKSIMAL", 0)

            view.findViewById<TextView>(R.id.tv_title).text = nama
            view.findViewById<TextView>(R.id.tv_amount_saved).text = "Rp$nominal"
            view.findViewById<TextView>(R.id.tv_target).text = "Maksimal: Rp$maksimal"

            // Hitung persentase
            val persenTextView = view.findViewById<TextView>(R.id.persen)
            val percentage = if (maksimal > 0) {
                (nominal?.toDouble()?.div(maksimal) ?: 0.0) * 100
            } else {
                0.0
            }
            persenTextView.text = String.format("%.1f%%", percentage)
        }

        val saveButton = view.findViewById<Button>(R.id.btn_save)
        val addAmountEditText = view.findViewById<EditText>(R.id.et_add_amount)

        saveButton.setOnClickListener {
            val newAmount = addAmountEditText.text.toString().toLongOrNull()

            if (newAmount != null && newAmount > 0) {
                val userId = auth.currentUser?.uid
                val nama = arguments?.getString("NAMA")

                if (userId != null && nama != null) {
                    updateFirestoreData(userId, nama, newAmount)
                } else {
                    Toast.makeText(requireContext(), "User atau nama tabungan tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Masukkan nominal yang valid", Toast.LENGTH_SHORT).show()
            }
        }

        // Tambahkan listener klik untuk iv_icon
        view.findViewById<ImageView>(R.id.iv_icon).setOnClickListener {
            dismiss() // Menutup dialog
        }

        return view
    }

    private fun updateFirestoreData(userId: String, nama: String, newAmount: Long) {
        val tabunganRef = firestore.collection("Tabungan")
            .document(userId)
            .collection("user_data")
            .whereEqualTo("nama", nama)

        tabunganRef.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documentId = documents.documents[0].id
                    firestore.collection("Tabungan")
                        .document(userId)
                        .collection("user_data")
                        .document(documentId)
                        .update("nominal", newAmount)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Nominal berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            dismiss() // Menutup dialog setelah berhasil
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Gagal memperbarui data: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Data tabungan tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Gagal mendapatkan data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar)
    }
}
