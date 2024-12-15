package com.example.capstonesejahtera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.news_title)
        val description: TextView = view.findViewById(R.id.news_description)
        val imageView: ImageView = view.findViewById(R.id.news_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.title.text = newsItem.title
        holder.description.text = newsItem.description
        Glide.with(holder.itemView.context).load(newsItem.urlToImage).into(holder.imageView)

        // Tambahkan klik listener
        holder.itemView.setOnClickListener {
            val fragment = HalamanBukaBerita()
            val bundle = Bundle()
            bundle.putString("title", newsItem.title)
            bundle.putString("description", newsItem.description)
            bundle.putString("urlToImage", newsItem.urlToImage)
            bundle.putString("urlArticle", newsItem.url) // Tambahkan URL artikel
            fragment.arguments = bundle

            val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


    override fun getItemCount(): Int {
        return newsList.size
    }
}
