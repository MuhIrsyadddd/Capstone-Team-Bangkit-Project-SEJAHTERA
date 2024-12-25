package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PopUpProgressTabungan : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan layout yang sesuai untuk dialog
        val view = inflater.inflate(R.layout.activity_pop_up_progress_tabungan, container, false)

        // Set padding untuk menghindari overlapped dengan sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.popupupdate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            // Mengatur ukuran dialog
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menyembunyikan judul dialog
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar)
    }
}
