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

        // Ambil UID pengguna saat ini
        val currentUser = auth.currentUser
        currentUser?.uid?.let { uid ->
            fetchNominalPengeluaran(uid)
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

    private fun fetchNominalPengeluaran(userId: String) {
        // Referensi ke koleksi Firestore
        val userCatatanRef = firestore.collection("Catatan").document(userId)
            .collection("user_catatan")

        userCatatanRef.get()
            .addOnSuccessListener { documents ->
                var totalPengeluaran = 0
                for (document in documents) {
                    val pengeluaran = document.getLong("nominal_pengeluaran") ?: 0
                    totalPengeluaran += pengeluaran.toInt()
                }
                pengeluaranTextView.text = "Rp$totalPengeluaran"
            }
            .addOnFailureListener { exception ->
                Log.e("DompetFragment", "Error mendapatkan data", exception)
            }
    }

    private fun openPopCatatanTabungan() {
        // Membuka halaman PopCatatanTabungan (misalnya sebagai fragment)
        val popTabunganFragment = PopUpCatatanTabungan()
        popTabunganFragment.show(requireFragmentManager(), "PopCatatanTabungan")
    }
}
