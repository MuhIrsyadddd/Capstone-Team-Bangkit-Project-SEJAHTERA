import android.annotation.SuppressLint
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
    private lateinit var tabunganTextView: TextView
    private lateinit var catatanLayout: ViewGroup

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

        // Ambil UID pengguna saat ini
        val currentUser = auth.currentUser
        currentUser?.uid?.let { uid ->
            fetchNominal(uid)  // Ambil nominal pengeluaran
            fetchTabungan(uid) // Ambil nominal tabungan
            fetchCatatan(uid)  // Ambil data catatan
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
                var totalTabungan = 0L
                for (document in documents) {
                    val nominal = document.getLong("nominal") ?: 0
                    totalTabungan += nominal
                }
                tabunganTextView.text = "Rp$totalTabungan"
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data tabungan", exception)
            }
    }

    private fun fetchCatatan(userId: String) {
        val catatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_data")

        catatanRef.get()
            .addOnSuccessListener { documents ->
                catatanLayout.removeAllViews() // Hapus item sebelumnya untuk menghindari duplikasi
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

                    // Tambahkan item ke layout
                    catatanLayout.addView(itemView)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data catatan", exception)
            }
    }

    private fun openPopCatatanTabungan() {
        val popTabunganFragment = PopUpCatatanTabungan()
        popTabunganFragment.show(requireFragmentManager(), "PopCatatanTabungan")
    }
}
