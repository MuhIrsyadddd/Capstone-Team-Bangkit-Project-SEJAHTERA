package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SahamActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saham)

        // Menyesuaikan padding dengan insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sahamhal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigasi ke AcesSahamActivity saat text_aces diklik
        val textAces = findViewById<TextView>(R.id.text_aces)
        textAces.setOnClickListener {
            val intent = Intent(this, AcesSaham::class.java)
            startActivity(intent)
        }
    }
}
