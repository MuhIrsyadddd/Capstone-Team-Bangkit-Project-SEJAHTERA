package com.example.capstonesejahtera

data class NewsItem(
    val title: String,           // Judul berita
    val description: String,     // Deskripsi berita
    val urlToImage: String       // URL gambar berita
)

data class NewsResponse(
    val articles: List<NewsItem> // List dari NewsItem
)
