package com.example.capstonesejahtera
data class NewsItem(
    val title: String,           // Judul berita
    val description: String,     // Deskripsi berita
    val urlToImage: String,      // URL gambar berita
    val url: String              // URL artikel
)

data class NewsResponse(
    val articles: List<NewsItem> // List dari NewsItem
)
