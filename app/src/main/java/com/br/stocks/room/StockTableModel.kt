package com.br.stocks.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.br.stocks.models.DataItem

@Entity(tableName = "stockTable")
class StockTableModel(
    @ColumnInfo(name = "type") var type: String? = null,
    @TypeConverters(DataTypeConverter::class)
    @ColumnInfo(name = "type_values") var values: List<DataItem>? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}