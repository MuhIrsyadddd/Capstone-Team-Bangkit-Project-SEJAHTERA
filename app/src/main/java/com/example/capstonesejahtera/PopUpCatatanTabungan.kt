package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class PopUpCatatanTabungan : DialogFragment() {

    private lateinit var editNamaCatatan: EditText
    private lateinit var editNominalPengeluaran: EditText
    private lateinit var editMaksimalTabungan: EditText
    private lateinit var textTabungan: TextView
    private lateinit var textCatatan: TextView
    private lateinit var imageRectangle7: View
    private lateinit var imageRectangle6: View

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_popupcatatantabungan, container, false)

        // Inisialisasi view
        editNamaCatatan = view.findViewById(R.id.edit_nama_catatan)
        editNominalPengeluaran = view.findViewById(R.id.edit_nominal_pengeluaran)
        editMaksimalTabungan = view.findViewById(R.id.edit_maximal_tabungan) // Inisialisasi EditText maksimal tabungan
        textTabungan = view.findViewById(R.id.text_tabungan)
        textCatatan = view.findViewById(R.id.text_catatan)
        imageRectangle7 = view.findViewById(R.id.image_rectangle7)
        imageRectangle6 = view.findViewById(R.id.image_rectangle6)

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Menyembunyikan EditText maksimal tabungan pada awal
        editMaksimalTabungan.visibility = View.GONE

        // Listener untuk Tabungan
        textTabungan.setOnClickListener {
            // Ganti background dan warna teks
            imageRectangle7.setBackgroundResource(R.drawable.image_rectangle66c)
            imageRectangle6.setBackgroundResource(R.drawable.image_rectangle7)
            textTabungan.setTextColor(resources.getColor(R.color.white))
            textCatatan.setTextColor(resources.getColor(R.color.black))

            // Ganti hint pada EditText
            editNamaCatatan.hint = "Nama Tabungan"
            editNominalPengeluaran.hint = "Nominal Tabungan"
            editMaksimalTabungan.visibility = View.VISIBLE // Tampilkan EditText maksimal tabungan
            editMaksimalTabungan.hint = "Maksimal Tabungan"

            // Sembunyikan EditText maksimal tabungan ketika Catatan dipilih
            editMaksimalTabungan.text.clear()
        }

        // Listener untuk Catatan
        textCatatan.setOnClickListener {
            // Ganti background dan warna teks
            imageRectangle6.setBackgroundResource(R.drawable.image_rectangle66c)
            imageRectangle7.setBackgroundResource(R.drawable.image_rectangle7)
            textCatatan.setTextColor(resources.getColor(R.color.white))
            textTabungan.setTextColor(resources.getColor(R.color.black))

            // Ganti hint pada EditText
            editNamaCatatan.hint = "Nama Catatan"
            editNominalPengeluaran.hint = "Nominal Pengeluaran"
            editMaksimalTabungan.visibility = View.GONE // Sembunyikan EditText maksimal tabungan
        }

        view.findViewById<View>(R.id.image_rectangle5).setOnClickListener {
            val namaCatatan = editNamaCatatan.text.toString()
            val nominalPengeluaranString = editNominalPengeluaran.text.toString()
            val maksimalTabunganString = editMaksimalTabungan.text.toString()

            if (namaCatatan.isNotEmpty() && nominalPengeluaranString.isNotEmpty()) {
                // Konversi nominalPengeluaran menjadi Int
                val nominalPengeluaran = nominalPengeluaranString.toIntOrNull()
                val maksimalTabungan = maksimalTabunganString.toIntOrNull()

                if (nominalPengeluaran != null) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val data = hashMapOf(
                            "nama" to namaCatatan,
                            "nominal" to nominalPengeluaran
                        )

                        // Tentukan koleksi berdasarkan hint
                        val selectedHint = editNamaCatatan.hint.toString()
                        val collectionName = if (selectedHint == "Nama Tabungan") "Tabungan" else "Catatan"

                        // Tambahkan maksimal tabungan ke data jika Tabungan yang dipilih
                        if (selectedHint == "Nama Tabungan" && maksimalTabungan != null) {
                            data["maksimal"] = maksimalTabungan
                        }

                        firestore.collection(collectionName).document(userId)
                            .collection("user_data").add(data)
                            .addOnSuccessListener {
                                Toast.makeText(context, "$collectionName berhasil dibuat", Toast.LENGTH_SHORT).show()
                                dismiss() // Tutup dialog setelah berhasil
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Gagal menyimpan ke $collectionName: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "User tidak terautentikasi", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Nominal harus berupa angka", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Nama dan Nominal tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.popup_width),
            resources.getDimensionPixelSize(R.dimen.popup_height)
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
