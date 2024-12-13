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
        return inflater.inflate(R.layout.fragment_investasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke SahamActivity
        val clickListener = View.OnClickListener {
            val intent = Intent(requireContext(), SahamActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.image_logosaham).setOnClickListener(clickListener)
        view.findViewById<View>(R.id.text_saham_terbaik).setOnClickListener(clickListener)
        view.findViewById<View>(R.id.text_investasi_saham).setOnClickListener(clickListener)
        view.findViewById<View>(R.id.masuksaham).setOnClickListener(clickListener)

        // Navigasi ke EmasActivity
        val emasClickListener = View.OnClickListener {
            val intent = Intent(requireContext(), EmasActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.logo_emas).setOnClickListener(emasClickListener)
        view.findViewById<View>(R.id.text_emas_untukmu).setOnClickListener(emasClickListener)
        view.findViewById<View>(R.id.text_investasi_emas).setOnClickListener(emasClickListener)
        view.findViewById<View>(R.id.masukemas).setOnClickListener(emasClickListener)
    }
}
