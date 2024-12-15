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

    @GET("predict/stock/CPIN")
    fun getCpinStockData(): Call<SahamResponse>

    @GET("predict/stock/EMTK")
    fun getEmtkStockData(): Call<SahamResponse>

    @GET("predict/stock/ESSA")
    fun getEssaStockData(): Call<SahamResponse>

    @GET("predict/stock/EXCL")
    fun getExclStockData(): Call<SahamResponse>

    @GET("predict/stock/GGRM")
    fun getGgrmStockData(): Call<SahamResponse>

    @GET("predict/stock/HRUM")
    fun getHrumStockData(): Call<SahamResponse>

    @GET("predict/stock/ICBP")
    fun getIcbpStockData(): Call<SahamResponse>

    @GET("predict/stock/INCO")
    fun getIncoStockData(): Call<SahamResponse>

}

data class PredictionRequest(
    val income: Double,
    val expenses: Double
)