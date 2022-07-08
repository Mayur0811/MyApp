package com.br.stocks.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.br.stocks.models.*
import javax.inject.Inject

class DataBaseRepositoryImpl @Inject constructor(var daoAccess: DAOAccess) {

    suspend fun insertStockData(stockDataResponse: StockDataResponse) = daoAccess.insertStockData(stockDataResponse)

    fun getStockData()= daoAccess.getStockData()

//    fun upDateStockData(stockDataResponse: StockDataResponse)=daoAccess.upDateStockData(stockDataResponse)

    suspend fun delete()=daoAccess.delete()

}