package com.br.stocks.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stockTable")
data class StockDataResponse(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("statuscode")
    val statuscode: Int? = null,

    @field:SerializedName("smallcap")
    val smallcap: List<DataItem>? = null,

    @field:SerializedName("data")
    val data: List<DataItem>? = null,

    @field:SerializedName("midcap")
    val midcap: List<DataItem>? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("bluechip")
    val bluechip: List<DataItem>? = null,

    @field:SerializedName("versionCode")
    val versionCode: Int,

    @field:SerializedName("showAd")
    val showAd: Boolean
)

data class DataItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("company_type")
    val companyType: String? = null,

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("stock_name")
    val stockName: String? = null,

    @field:SerializedName("stop_loss")
    val stopLoss: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("target")
    val target: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Any? = null,

    @field:SerializedName("share_type")
    val shareType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("trade_type")
    val tradeType: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
data class PostData(
    @field:SerializedName("status")
    val status:Int,
    @field:SerializedName("msg")
    val msg:String,
    @field:SerializedName("post")
    val data:DataItem,
    @field:SerializedName("statuscode")
    val statusCode:Int
)
