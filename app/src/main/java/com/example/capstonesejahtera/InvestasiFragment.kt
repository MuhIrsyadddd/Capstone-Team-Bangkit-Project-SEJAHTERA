package com.example.capstonesejahtera

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class InvestasiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_investasi, container, false)

        // Set up click listeners
        view.findViewById<View>(R.id.logo_emas).setOnClickListener {
            navigateToSahamActivity()
        }
        view.findViewById<View>(R.id.text_emas_untukmu).setOnClickListener {
            navigateToSahamActivity()
        }
        view.findViewById<View>(R.id.masuksaham).setOnClickListener {
            navigateToSahamActivity()
        }
        view.findViewById<View>(R.id.text_investasi_emas).setOnClickListener {
            navigateToSahamActivity()
        }

        return view
    }

    private fun navigateToSahamActivity() {
        val intent = Intent(requireContext(), SahamActivity::class.java)
        startActivity(intent)
    }
}
