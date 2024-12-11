package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.edit_email)
        val passwordEditText: EditText = findViewById(R.id.edit_password)

        val signInTextView: TextView = findViewById(R.id.button_sign_in)
        signInTextView.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses login menggunakan Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        checkUserStatus()
                    } else {
                        Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Listener untuk klik "Sign Up"
        val signUpTextView: TextView = findViewById(R.id.text_sign_up_prompt)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, Registrasi::class.java)
            startActivity(intent)
        }

        // Setup Google Sign-In
        val googleSignInButton: TextView = findViewById(R.id.text_google_login)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Masukkan ID klien Anda
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in gagal: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        checkUserStatus()
                    } else {
                        Toast.makeText(this, "Autentikasi Google gagal.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkUserStatus() {
        val user = mAuth.currentUser
        if (user != null) {
            val userRef = db.collection("users").document(user.uid)
            userRef.get().addOnSuccessListener { document ->
                if (!document.exists()) {
                    // Pertama kali login, arahkan ke DataProfileActivity
                    val intent = Intent(this, DataProfile::class.java)
                    startActivity(intent)

                    // Simpan status pengguna ke Firestore
                    val userData = hashMapOf(
                        "isFirstLogin" to false,
                        "email" to user.email,
                        "displayName" to user.displayName
                    )
                    userRef.set(userData)
                } else {
                    // Sudah login sebelumnya, arahkan ke DashboardActivity
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal memeriksa status pengguna.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
