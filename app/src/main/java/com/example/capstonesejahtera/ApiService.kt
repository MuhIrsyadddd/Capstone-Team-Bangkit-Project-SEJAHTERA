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

    @GET("predict/stock/INDF")
    fun getIndfStockData(): Call<SahamResponse>

    @GET("predict/stock/INKP")
    fun getInkpStockData(): Call<SahamResponse>

    @GET("predict/stock/INTP")
    fun getIntpStockData(): Call<SahamResponse>

    @GET("predict/stock/ISAT")
    fun getIsatStockData(): Call<SahamResponse>

    @GET("predict/stock/ITMG")
    fun getItmgStockData(): Call<SahamResponse>

    @GET("predict/stock/JPFA")
    fun getJpfaStockData(): Call<SahamResponse>

    @GET("predict/stock/JSMR")
    fun getJsmrStockData(): Call<SahamResponse>

    @GET("predict/stock/KLBF")
    fun getKlbfStockData(): Call<SahamResponse>

    @GET("predict/stock/MAPI")
    fun getMapiStockData(): Call<SahamResponse>

    @GET("predict/stock/MBMA")
    fun getMbmaStockData(): Call<SahamResponse>

    @GET("predict/stock/MDKA")
    fun getMdkaStockData(): Call<SahamResponse>

    @GET("predict/stock/MEDC")
    fun getMedcStockData(): Call<SahamResponse>

    @GET("predict/stock/MTEL")
    fun getMtelStockData(): Call<SahamResponse>

    @GET("predict/stock/PGAS")
    fun getPgasStockData(): Call<SahamResponse>

    @GET("predict/stock/PGEO")
    fun getPgeoStockData(): Call<SahamResponse>

    @GET("predict/stock/PTBA")
    fun getPtbaStockData(): Call<SahamResponse>

    @GET("predict/stock/PTMP")
    fun getPtmpStockData(): Call<SahamResponse>

    @GET("predict/stock/SCMA")
    fun getScmaStockData(): Call<SahamResponse>

    @GET("predict/stock/SIDO")
    fun getSidoStockData(): Call<SahamResponse>

    @GET("predict/stock/SMGR")
    fun getSmgrStockData(): Call<SahamResponse>

    @GET("predict/stock/SRTG")
    fun getSrtgStockData(): Call<SahamResponse>

    @GET("predict/stock/TBIG")
    fun getTbigStockData(): Call<SahamResponse>

    @GET("predict/stock/TINS")
    fun getTinsStockData(): Call<SahamResponse>

    @GET("predict/stock/TOWR")
    fun getTowrStockData(): Call<SahamResponse>

    @GET("predict/stock/TPIA")
    fun getTpiaStockData(): Call<SahamResponse>

    @GET("predict/stock/UNTR")
    fun getUntrStockData(): Call<SahamResponse>

    @GET("predict/stock/UNVR")
    fun getUnvrStockData(): Call<SahamResponse>

}

data class PredictionRequest(
    val income: Double,
    val expenses: Double
)