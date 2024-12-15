package com.example.capstonesejahtera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class HalamanBukaBerita : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_halaman_buka_berita, container, false)

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val actionButton: Button = view.findViewById(R.id.actionButton)

        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val urlToImage = arguments?.getString("urlToImage")
        val urlArticle = arguments?.getString("urlArticle") // Tambahkan ini

        titleTextView.text = title ?: "No Title"
        descriptionTextView.text = description ?: "No Description"

        if (!urlToImage.isNullOrEmpty()) {
            Glide.with(this).load(urlToImage).into(imageView)
        }

        // Atur listener untuk tombol "Baca Selengkapnya"
        actionButton.setOnClickListener {
            urlArticle?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        return view
    }
}
