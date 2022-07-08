package com.br.stocks.room

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.br.stocks.models.*
import com.br.stocks.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DataBaseRepository @Inject constructor(var dataBaseRepositoryImpl: DataBaseRepositoryImpl) {

    suspend fun insertStockData(stockDataResponse: StockDataResponse) =
        withContext(Dispatchers.IO) {
            dataBaseRepositoryImpl.insertStockData(stockDataResponse)
        }

    suspend fun delete()= withContext(Dispatchers.IO){
        dataBaseRepositoryImpl.delete()
    }

     fun getStockData(): Flow<StockDataResponse> {
        return flow {
            emit( dataBaseRepositoryImpl.getStockData() )
        }.flowOn(Dispatchers.IO)
    }


//    suspend fun upDateStockData(stockDataResponse: StockDataResponse)= withContext(Dispatchers.IO){
//        dataBaseRepositoryImpl.upDateStockData(stockDataResponse)
//    }

}