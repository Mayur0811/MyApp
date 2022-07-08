package com.br.stocks.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.br.stocks.models.StockDataResponse

@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStockData(stockDataResponse: StockDataResponse)

    @Query("SELECT * FROM stockTable")
    fun getStockData() : StockDataResponse

    @Query("DELETE FROM stockTable")
    suspend fun delete()

//    @Update(onConflict = OnConflictStrategy.IGNORE)
//    fun upDateStockData(stockDataResponse: StockDataResponse)
//

}