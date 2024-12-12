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
    private lateinit var tabunganTextView: TextView // Tambahkan ini untuk tabungan
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

        // Hubungkan TextView untuk tabungan
        tabunganTextView = view.findViewById(R.id.tabungan1) // Hubungkan dengan tabungan

        // Hubungkan dengan layout untuk catatan
        catatanLayout = view.findViewById(R.id.catatanLayout)

        // Ambil UID pengguna saat ini
        val currentUser = auth.currentUser
        currentUser?.uid?.let { uid ->
            fetchNominal(uid) // Ambil nominal pengeluaran
            fetchTabungan(uid) // Ambil nominal tabungan
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

    private fun fetchNominal(userId: String) {
        // Referensi ke koleksi Firestore
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
        // Referensi ke koleksi Firestore untuk tabungan
        val tabunganRef = firestore.collection("Tabungan").document(userId)
            .collection("user_data")

        tabunganRef.get()
            .addOnSuccessListener { documents ->
                var totalTabungan = 0L
                for (document in documents) {
                    val nominal = document.getLong("nominal") ?: 0
                    totalTabungan += nominal
                }
                // Tampilkan total nominal tabungan di TextView
                tabunganTextView.text = "Rp$totalTabungan"
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
