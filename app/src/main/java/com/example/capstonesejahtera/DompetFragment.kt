import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstonesejahtera.PopUpCatatanTabungan
import com.example.capstonesejahtera.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DompetFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var pengeluaranTextView: TextView
    private lateinit var catatanLayout: ViewGroup // Tambahkan ini untuk layout catatan

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dompet, container, false)

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Hubungkan TextView
        pengeluaranTextView = view.findViewById(R.id.pengeluaran1)

        // Hubungkan dengan layout untuk catatan
        catatanLayout = view.findViewById(R.id.catatanLayout) // Pastikan ID ini adalah ViewGroup yang tepat

        // Ambil UID pengguna saat ini
        val currentUser = auth.currentUser
        currentUser?.uid?.let { uid ->
            fetchNominalPengeluaran(uid) // Ambil nominal pengeluaran
            fetchCatatan(uid) // Panggil metode fetchCatatan
        } ?: run {
            Log.e("DompetFragment", "User tidak ditemukan")
        }

        // Tambahkan listener untuk tab uploadCat
        val uploadCatButton: View = view.findViewById(R.id.uploadcattab) // Ganti dengan ID yang sesuai
        uploadCatButton.setOnClickListener {
            openPopCatatanTabungan()
        }

        return view
    }

    private fun fetchCatatan(userId: String) {
        // Referensi ke koleksi Firestore
        val userCatatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_catatan")

        userCatatanRef.get()
            .addOnSuccessListener { documents ->
                var totalPengeluaran = 0L // Menyimpan total nominal pengeluaran
                for (document in documents) {
                    val namaCatatan = document.getString("nama_catatan") ?: "Tanpa Nama"
                    val nominalPengeluaran = document.getLong("nominal_pengeluaran") ?: 0

                    // Inflasi tampilan item catatan dari layout yang baru dibuat
                    val catatanView = LayoutInflater.from(requireContext()).inflate(R.layout.item_catatan, catatanLayout, false)

                    // Hubungkan TextView di dalam item catatan
                    val namaCatatanTextView: TextView = catatanView.findViewById(R.id.nama_catatan)
                    val nominalPengeluaranTextView: TextView = catatanView.findViewById(R.id.nominal_pengeluaran)

                    // Atur teks untuk nama catatan dan nominal pengeluaran
                    namaCatatanTextView.text = namaCatatan
                    nominalPengeluaranTextView.text = "Rp$nominalPengeluaran"

                    // Tambahkan tampilan item catatan ke catatanLayout
                    catatanLayout.addView(catatanView)

                    // Tambahkan ke total nominal pengeluaran
                    totalPengeluaran += nominalPengeluaran
                }
                // Setelah semua catatan diambil, atur total ke TextView
                pengeluaranTextView.text = "Rp$totalPengeluaran" // Menampilkan total nominal pengeluaran
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data catatan", exception)
            }
    }

    private fun fetchNominalPengeluaran(userId: String) {
        // Fungsi ini tidak lagi diperlukan jika nominal pengeluaran dihitung di fetchCatatan
        // Anda bisa menghapusnya jika tidak ada kebutuhan lain untuk fungsi ini
    }

    private fun openPopCatatanTabungan() {
        // Membuka halaman PopCatatanTabungan (misalnya sebagai fragment)
        val popTabunganFragment = PopUpCatatanTabungan()
        popTabunganFragment.show(requireFragmentManager(), "PopCatatanTabungan")
    }
}

