package com.br.stocks.api

import com.br.stocks.models.*
import com.br.stocks.utils.Constant
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET
    suspend fun getEvents(@Url url: String?) : Response<ArrayList<EventResponse>>

    @GET
    suspend fun getStockData(@Url url:String?): Response<StockDataResponse>

    @GET
    suspend fun getMutualFundNews(@Url url:String?): Response<NewsResponse>

    @GET
    suspend fun getEconomyNews(@Url url:String?): Response<NewsResponse>

    @GET
    suspend fun getInsuranceNews(@Url url:String?): Response<InsuranceNewsResponse>

    @GET
    suspend fun getIPONews(@Url url:String?): Response<NewsResponse>

    @GET
    suspend fun getForeignMarketNews(@Url url:String?): Response<NewsResponse>

    @GET
    suspend fun getStockByCategory(@Url url:String?): Response<StockByCategoryResponse>

    @GET
    suspend fun getNotification(@Url url:String?): Response<NotificationResponse>


    @POST(Constant.registerDevice)
    suspend fun registerDevice(@Body body:RequestBody):Response<RegisterDeviceResponse>

    @GET
    suspend fun getPreSession(@Url session:String?) :Response<PreSessionResponse>

    @GET(Constant.notificationStockApi+"/{id}")
    suspend fun getPost(@Path("id") post_id:String): Response<PostData>
}