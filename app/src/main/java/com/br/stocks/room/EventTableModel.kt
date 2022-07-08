package com.br.stocks.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.br.stocks.models.DataItem
import com.br.stocks.models.EventResponse

@Entity(tableName = "eventTable")
class EventTableModel(
//    @ColumnInfo(name = "news") var eventResponse: List<EventResponse>?=null

    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}