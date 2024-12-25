package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PopUpRubahCatatan : DialogFragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_pop_up_rubah_catatan, container, false)

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Ambil data dari arguments
        val namaCatatan = arguments?.getString("NAMA_CATATAN") ?: "Tidak ada nama"
        val nominalCatatan = arguments?.getLong("NOMINAL_CATATAN") ?: 0L
        val catatanId = arguments?.getString("CATATAN_ID") ?: ""

        // Hubungkan tampilan
        val namaTextView = view.findViewById<TextView>(R.id.namapengeluaran)
        val nominalTextView = view.findViewById<TextView>(R.id.datanominal) // Untuk menampilkan nominal sebelumnya
        val nominalEditText = view.findViewById<EditText>(R.id.edit_nominal_pengeluaran) // Untuk memasukkan nominal baru
        val saveButton = view.findViewById<Button>(R.id.buttonubah)
        val closeButton = view.findViewById<ImageView>(R.id.image_vector5)

        // Tampilkan data ke tampilan
        namaTextView.text = namaCatatan
        nominalTextView.text = "Rp$nominalCatatan" // Tampilkan nominal sebelumnya
        nominalEditText.setText(nominalCatatan.toString())

        // Tambahkan klik listener ke closeButton
        closeButton.setOnClickListener {
            dismiss() // Tutup dialog
        }

        // Tambahkan klik listener ke saveButton
        saveButton.setOnClickListener {
            val newNominal = nominalEditText.text.toString().toLongOrNull() ?: nominalCatatan
            updateNominal(catatanId, newNominal)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Mengatur ukuran dialog
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.popup_width),
            resources.getDimensionPixelSize(R.dimen.popup_height)
        )
    }

    private fun updateNominal(catatanId: String, newNominal: Long) {
        val userId = auth.currentUser?.uid ?: return

        // Referensi dokumen catatan
        val catatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_data").document(catatanId)

        // Perbarui data
        catatanRef.update("nominal", newNominal)
            .addOnSuccessListener {
                Log.d("PopUpRubahCatatan", "Nominal berhasil diperbarui")
                dismiss() // Tutup dialog setelah berhasil
            }
            .addOnFailureListener { exception ->
                Log.e("PopUpRubahCatatan", "Gagal memperbarui nominal", exception)
            }
    }
}
