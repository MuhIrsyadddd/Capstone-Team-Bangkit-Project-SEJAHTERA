package com.example.capstonesejahtera

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/news/business")
    fun getBusinessNews(): Call<NewsResponse>
}
