package com.br.stocks.api

import okhttp3.RequestBody
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(var apiService: ApiService) {

    suspend fun getEvents(url:String)=apiService.getEvents(url)

    suspend fun getStockData(url:String)=apiService.getStockData(url)

    suspend fun getMutualFundNews(url:String) = apiService.getMutualFundNews(url)

    suspend fun getEconomyNews(url:String) = apiService.getEconomyNews(url)

    suspend fun getInsuranceNews(url:String) = apiService.getInsuranceNews(url)

    suspend fun getIPONews(url:String) = apiService.getIPONews(url)

    suspend fun getForeignMarketNews(url:String) = apiService.getForeignMarketNews(url)

    suspend fun getStockByCategory(url:String) = apiService.getStockByCategory(url)

    suspend fun getNotification(url:String) = apiService.getNotification(url)

    suspend fun registerDevice(body: RequestBody)=apiService.registerDevice(body)

    suspend fun getPreSession(session: String)=apiService.getPreSession(session)

    suspend fun getPost(post_id:String)=apiService.getPost(post_id)
}