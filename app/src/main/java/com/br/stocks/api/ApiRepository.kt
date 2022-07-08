package com.br.stocks.api

import com.br.stocks.models.*
import com.br.stocks.utils.BaseApiResponse
import com.br.stocks.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import javax.inject.Inject

class ApiRepository @Inject constructor(private var apiServiceImpl: ApiServiceImpl) :BaseApiResponse(){

    suspend fun getEvents(url: String): Flow<NetworkResult<ArrayList<EventResponse>>> {
        return flow {
            emit(safeApiCall { apiServiceImpl.getEvents(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStockData(url: String):Flow<NetworkResult<StockDataResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getStockData(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMutualFundNews(url:String) :Flow<NetworkResult<NewsResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getMutualFundNews(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEconomyNews(url:String) :Flow<NetworkResult<NewsResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getEconomyNews(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getInsuranceNews(url:String) :Flow<NetworkResult<InsuranceNewsResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getInsuranceNews(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getIPONews(url:String) :Flow<NetworkResult<NewsResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getIPONews(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getForeignMarketNews(url:String) :Flow<NetworkResult<NewsResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getForeignMarketNews(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStockByCategory(url:String) :Flow<NetworkResult<StockByCategoryResponse>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getStockByCategory(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerDevice(body: RequestBody):Flow<NetworkResult<RegisterDeviceResponse>>{
        return  flow {
            emit(safeApiCall { apiServiceImpl.registerDevice(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNotification(url: String):Flow<NetworkResult<NotificationResponse>>{
        return  flow {
            emit(safeApiCall { apiServiceImpl.getNotification(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPreSession(session: String):Flow<NetworkResult<PreSessionResponse>>{
        return  flow {
            emit(safeApiCall { apiServiceImpl.getPreSession(session) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPost(post_id:String):Flow<NetworkResult<PostData>>{
        return flow {
            emit(safeApiCall { apiServiceImpl.getPost(post_id) })
        }.flowOn(Dispatchers.IO)
    }
}