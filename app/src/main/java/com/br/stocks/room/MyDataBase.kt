package com.br.stocks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.br.stocks.models.*

@Database(entities = [StockDataResponse::class], version = 1,exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class MyDataBase : RoomDatabase() {
    abstract fun dao(): DAOAccess
}