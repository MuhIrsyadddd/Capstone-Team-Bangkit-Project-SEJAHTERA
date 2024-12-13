package com.example.capstonesejahtera

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/news/business")
    fun getBusinessNews(): Call<NewsResponse>

    @GET("predict/stock/ACES")
    fun getAcesStockData(): Call<SahamAcesResponse>

    @POST("predict/gold")
    fun predictGold(@Body request: PredictionRequest): Call<EmasResponse>

}

data class PredictionRequest(
    val income: Double,
    val expenses: Double
)