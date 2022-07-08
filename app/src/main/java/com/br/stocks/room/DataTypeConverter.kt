package com.br.stocks.room

import androidx.room.TypeConverter
import com.br.stocks.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DataTypeConverter {

    var gson=Gson()

    @TypeConverter
    fun fromList(item: List<DataItem>): String {
        return gson.toJson(item)
    }

    @TypeConverter
    fun toList(string: String): List<DataItem> {
        val type: Type = object :
            TypeToken<List<DataItem>>() {}.type
        return gson.fromJson(string,type)
    }

}