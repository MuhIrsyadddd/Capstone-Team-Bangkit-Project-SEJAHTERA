package com.example.capstonesejahtera

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var sharedPreferences: SharedPreferences
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val emailEditText: EditText = findViewById(R.id.edit_email)
        val passwordEditText: EditText = findViewById(R.id.edit_password)
        val signInTextView: TextView = findViewById(R.id.button_sign_in)
        val signUpTextView: TextView = findViewById(R.id.text_sign_up_prompt)
        val googleSignInButton: TextView = findViewById(R.id.text_google_login)

        signInTextView.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveLoginStatus()
                        checkUserStatus()
                    } else {
                        Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signUpTextView.setOnClickListener {
            val intent = Intent(this, Registrasi::class.java)
            startActivity(intent)
        }

        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Animasi
        setupAnimations()
    }

    private fun setupAnimations() {
        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val textLoginAccount: TextView = findViewById(R.id.text_login_account)
        val editEmail: EditText = findViewById(R.id.edit_email)
        val editPassword: EditText = findViewById(R.id.edit_password)
        val buttonSignIn: Button = findViewById(R.id.button_sign_in)
        val textSignUpPrompt: TextView = findViewById(R.id.text_sign_up_prompt)
        val textOrSignUp: TextView = findViewById(R.id.text_or_sign_up)
        val textGoogleLogin: TextView = findViewById(R.id.text_google_login)

        val logoAnimator = ObjectAnimator.ofFloat(logoImageView, "translationY", -200f, 0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val textAnimator = ObjectAnimator.ofFloat(textLoginAccount, "alpha", 0f, 1f).apply {
            duration = 1000
            startDelay = 300
        }

        val emailAnimator = ObjectAnimator.ofFloat(editEmail, "translationX", -500f, 0f).apply {
            duration = 1000
            startDelay = 400
        }

        val passwordAnimator = ObjectAnimator.ofFloat(editPassword, "translationX", -500f, 0f).apply {
            duration = 1000
            startDelay = 500
        }

        val buttonAnimator = ObjectAnimator.ofFloat(buttonSignIn, "scaleX", 0.5f, 1f).apply {
            duration = 1000
            startDelay = 600
        }

        val signUpPromptAnimator = ObjectAnimator.ofFloat(textSignUpPrompt, "alpha", 0f, 1f).apply {
            duration = 1000
            startDelay = 700
        }

        val orSignUpAnimator = ObjectAnimator.ofFloat(textOrSignUp, "alpha", 0f, 1f).apply {
            duration = 1000
            startDelay = 800
        }

        val googleLoginAnimator = ObjectAnimator.ofFloat(textGoogleLogin, "alpha", 0f, 1f).apply {
            duration = 1000
            startDelay = 900
        }

        AnimatorSet().apply {
            playTogether(
                logoAnimator,
                textAnimator,
                emailAnimator,
                passwordAnimator,
                buttonAnimator,
                signUpPromptAnimator,
                orSignUpAnimator,
                googleLoginAnimator
            )
            start()
        }
    }

    private fun saveLoginStatus() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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
        account?.let {
            val credential = GoogleAuthProvider.getCredential(it.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveLoginStatus()
                        checkUserStatus()
                    } else {
                        Toast.makeText(this, "Autentikasi Google gagal.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkUserStatus() {
        val user = mAuth.currentUser
        user?.let {
            val userRef = db.collection("users").document(it.uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        val intent = Intent(this, DataProfile::class.java)
                        startActivity(intent)
                        val userData = hashMapOf(
                            "isFirstLogin" to false,
                            "email" to it.email,
                            "displayName" to it.displayName
                        )
                        userRef.set(userData)
                    } else {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memeriksa status pengguna.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
