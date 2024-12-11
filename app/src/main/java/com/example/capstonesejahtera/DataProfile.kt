package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import android.content.Intent


class DataProfile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_profile)

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val editDate: EditText = findViewById(R.id.edit_date)
        val spinnerGender: Spinner = findViewById(R.id.spinner_gender)
        val editName: EditText = findViewById(R.id.edit_email) // EditText untuk Nama Lengkap
        val buttonSignUp: Button = findViewById(R.id.button_sign_up)

        // Konfigurasi DatePickerDialog
        editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    editDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Konfigurasi Spinner Gender
        val genderOptions = listOf("Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )
        spinnerGender.adapter = adapter

        buttonSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val birthDate = editDate.text.toString().trim()
            val gender = spinnerGender.selectedItem.toString()

            if (name.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Dapatkan UID pengguna saat ini
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val userData = mapOf(
                    "name" to name,
                    "birthDate" to birthDate,
                    "gender" to gender
                )

                db.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        // Intent ke DashboardActivity
                        startActivity(Intent(this, ResikoSatu::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User tidak terautentikasi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
