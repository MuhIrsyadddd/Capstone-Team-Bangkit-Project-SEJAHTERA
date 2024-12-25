package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DompetFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var pengeluaranTextView: TextView
    private lateinit var tabunganTextView: TextView
    private lateinit var catatanLayout: ViewGroup
    private lateinit var totalPenghasilanPengeluaranTextView: TextView  // Tambahkan variabel untuk totalPenghasilanPengeluaran

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dompet, container, false)

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Hubungkan TextView dan layout
        pengeluaranTextView = view.findViewById(R.id.pengeluaran1)
        tabunganTextView = view.findViewById(R.id.tabungan1)
        catatanLayout = view.findViewById(R.id.catatanLayout)
        totalPenghasilanPengeluaranTextView = view.findViewById(R.id.totalpenghasilanpengeluaran)  // Hubungkan TextView

        // Ambil UID pengguna saat ini
        val currentUser = auth.currentUser
        currentUser?.uid?.let { uid ->
            fetchNominal(uid)  // Ambil nominal pengeluaran
            fetchTabungan(uid) // Ambil nominal tabungan
            fetchCatatan(uid)  // Ambil data catatan
            calculateTotalPenghasilanPengeluaran(uid) // Hitung total penghasilan dan pengeluaran
        } ?: run {
            Log.e("DompetFragment", "User tidak ditemukan")
        }

        // Listener untuk membuka pop-up
        val uploadCatButton: View = view.findViewById(R.id.uploadcattab)
        uploadCatButton.setOnClickListener {
            openPopCatatanTabungan()
        }

        return view
    }

    private fun fetchNominal(userId: String) {
        val userDataRef = firestore.collection("Catatan").document(userId)
            .collection("user_data")

        userDataRef.get()
            .addOnSuccessListener { documents ->
                var totalPengeluaran = 0L
                for (document in documents) {
                    val nominal = document.getLong("nominal") ?: 0
                    totalPengeluaran += nominal
                }
                pengeluaranTextView.text = "Rp$totalPengeluaran"
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data nominal", exception)
            }
    }

    private fun fetchTabungan(userId: String) {
        val tabunganRef = firestore.collection("Tabungan").document(userId)
            .collection("user_data")

        tabunganRef.get()
            .addOnSuccessListener { documents ->
                val textViewTabungan = view?.findViewById<TextView>(R.id.textViewTabungan)

                if (documents.isEmpty) {
                    // Sembunyikan textViewTabungan jika tidak ada data
                    textViewTabungan?.visibility = View.GONE
                } else {
                    // Tampilkan textViewTabungan jika ada data
                    textViewTabungan?.visibility = View.VISIBLE

                    var totalTabungan = 0L // Variabel untuk menghitung total nominal tabungan
                    val tabunganLayout = view?.findViewById<ViewGroup>(R.id.tabunganlayout11)
                    tabunganLayout?.removeAllViews()

                    for (document in documents) {
                        val nama = document.getString("nama") ?: "Tidak ada nama"
                        val nominal = document.getLong("nominal") ?: 0L

                        // Tambahkan ke total nominal tabungan
                        totalTabungan += nominal

                        // Inflasi layout untuk setiap item tabungan
                        val itemView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.item_tabungan, tabunganLayout, false)

                        // Atur nilai pada view
                        val namaTextView = itemView.findViewById<TextView>(R.id.nama_tabungan)
                        val nominalTextView = itemView.findViewById<TextView>(R.id.nominal_tabungan)

                        namaTextView.text = nama
                        nominalTextView.text = "Rp$nominal"

                        // Tambahkan listener klik pada itemView untuk membuka PopUpProgressTabungan
                        itemView.setOnClickListener {
                            openPopProgressTabungan()
                        }

                        // Tambahkan item ke dalam layout
                        tabunganLayout?.addView(itemView)
                    }

                    // Tampilkan total nominal tabungan di tabunganTextView
                    tabunganTextView.text = "Rp$totalTabungan"
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data tabungan", exception)
            }
    }

    // Fungsi untuk membuka PopUpProgressTabungan
    private fun openPopProgressTabungan() {
        val popProgressTabunganFragment = PopUpProgressTabungan()
        popProgressTabunganFragment.show(requireFragmentManager(), "PopProgressTabungan")
    }



    private fun fetchCatatan(userId: String) {
        val catatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_data")

        catatanRef.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Sembunyikan TextView jika tidak ada data
                    view?.findViewById<TextView>(R.id.textViewCatatan)?.visibility = View.GONE
                } else {
                    // Tampilkan TextView jika ada data
                    view?.findViewById<TextView>(R.id.textViewCatatan)?.visibility = View.VISIBLE

                    // Kosongkan layout untuk mencegah duplikasi
                    catatanLayout.removeAllViews()
                    for (document in documents) {
                        val nama = document.getString("nama") ?: "Tidak ada nama"
                        val nominal = document.getLong("nominal") ?: 0L

                        // Inflasi layout item_catatan.xml
                        val itemView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.item_catatan, catatanLayout, false)

                        // Atur nilai pada view
                        val namaCatatanTextView = itemView.findViewById<TextView>(R.id.nama_catatan)
                        val nominalPengeluaranTextView = itemView.findViewById<TextView>(R.id.nominal_pengeluaran)

                        namaCatatanTextView.text = nama
                        nominalPengeluaranTextView.text = "Rp$nominal"

                        // Tambahkan listener klik pada itemView
                        itemView.setOnClickListener {
                            // Mengambil ID dokumen catatan jika diperlukan
                            val catatanId = document.id // Ganti dengan ID dokumen yang sesuai
                            openPopRubahCatatan(catatanId)
                        }

                        // Tambahkan item ke layout
                        catatanLayout.addView(itemView)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data catatan", exception)
            }
    }

    private fun openPopRubahCatatan(catatanId: String) {
        val popRubahCatatanFragment = PopUpRubahCatatan()
        val args = Bundle()

        // Fetch data nama dan nominal
        val catatanRef = firestore.collection("Catatan").document(auth.currentUser!!.uid)
            .collection("user_data").document(catatanId)

        catatanRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nama = document.getString("nama") ?: "Tidak ada nama"
                    val nominal = document.getLong("nominal") ?: 0L

                    // Kirim data nama, nominal, dan ID ke PopUpRubahCatatan
                    args.putString("NAMA_CATATAN", nama)
                    args.putLong("NOMINAL_CATATAN", nominal)
                    args.putString("CATATAN_ID", catatanId)
                    popRubahCatatanFragment.arguments = args
                    popRubahCatatanFragment.show(requireFragmentManager(), "PopRubahCatatan")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error fetching catatan data", exception)
            }
    }





    private fun calculateTotalPenghasilanPengeluaran(userId: String) {
        val tabunganRef = firestore.collection("Tabungan").document(userId)
            .collection("user_data")

        val catatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_data")

        // Ambil nominal dari Tabungan
        tabunganRef.get()
            .addOnSuccessListener { tabunganDocuments ->
                var totalTabungan = 0L
                for (document in tabunganDocuments) {
                    val nominal = document.getLong("nominal") ?: 0L
                    totalTabungan += nominal
                }

                // Setelah mendapatkan total tabungan, ambil nominal dari Catatan
                catatanRef.get()
                    .addOnSuccessListener { catatanDocuments ->
                        var totalPengeluaran = 0L
                        for (document in catatanDocuments) {
                            val nominal = document.getLong("nominal") ?: 0L
                            totalPengeluaran += nominal
                        }

                        // Hitung total penghasilan dan pengeluaran
                        val totalPenghasilanPengeluaran = totalTabungan - totalPengeluaran

                        // Tampilkan hasil dengan tanda minus jika negatif
                        totalPenghasilanPengeluaranTextView.text = if (totalPenghasilanPengeluaran < 0) {
                            "Rp-${Math.abs(totalPenghasilanPengeluaran)}"
                        } else {
                            "Rp$totalPenghasilanPengeluaran"
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("DompetFragment", "Error mendapatkan data catatan", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data tabungan", exception)
            }
    }


    private fun openPopCatatanTabungan() {
        val popTabunganFragment = PopUpCatatanTabungan()
        popTabunganFragment.show(requireFragmentManager(), "PopCatatanTabungan")
    }
}