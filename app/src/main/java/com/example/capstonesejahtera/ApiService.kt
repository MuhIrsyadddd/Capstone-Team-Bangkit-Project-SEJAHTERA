package com.example.capstonesejahtera

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/news/business")
    fun getBusinessNews(): Call<NewsResponse>

    @GET("predict/stock/ACES")
    fun getAcesStockData(): Call<SahamResponse>

    @POST("predict/gold")
    fun predictGold(@Body request: PredictionRequest): Call<EmasResponse>

    @GET("predict/stock/ADRO")
    fun getAdroStockData(): Call<SahamResponse>

    @GET("predict/stock/AKRA")
    fun getAkraStockData(): Call<SahamResponse>

    @GET("predict/stock/AMMN")
    fun getAmmnStockData(): Call<SahamResponse>

    @GET("predict/stock/AMRT")
    fun getAmrtStockData(): Call<SahamResponse>

    @GET("predict/stock/ARTO")
    fun getArtoStockData(): Call<SahamResponse>

    @GET("predict/stock/BBNI")
    fun getBbniStockData(): Call<SahamResponse>

    @GET("predict/stock/BBTN")
    fun getBbtnStockData(): Call<SahamResponse>

    @GET("predict/stock/BMRI")
    fun getBmriStockData(): Call<SahamResponse>

    @GET("predict/stock/BRIS")
    fun getBrisStockData(): Call<SahamResponse>

    @GET("predict/stock/BRPT")
    fun getBrptStockData(): Call<SahamResponse>

    @GET("predict/stock/BUKA")
    fun getBukaStockData(): Call<SahamResponse>


}

data class PredictionRequest(
    val income: Double,
    val expenses: Double
)