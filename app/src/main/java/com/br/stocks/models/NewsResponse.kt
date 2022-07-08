package com.br.stocks.models

import com.google.gson.annotations.SerializedName


data class NewsResponse(
    @field:SerializedName("response")
    var response:ResponseData?
)
data class ResponseData(
    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("data")
    val data: DataList? = null,

)

data class DataList(
    @field:SerializedName("newslist")
    val newsList: News?
)

data class News(
    @field:SerializedName("recordcount")
    val recordCount:String?,
    @field:SerializedName("news")
    val news:ArrayList<NewsItem>
)

data class NewsItem(
    @field:SerializedName("section_name")
    val sectionName:String?,

    @field:SerializedName("date")
    val date:String?,

    @field:SerializedName("heading")
    val heading:String?,

    @field:SerializedName("caption")
    val caption:String?,

    @field:SerializedName("flag")
    val flag: String? = null,

    @field:SerializedName("sno")
    val sno: String? = null,

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("ind_code")
    val indCode: Any? = null,

    @field:SerializedName("co_code")
    val coCode: Any? = null,

    @field:SerializedName("arttext")
    val arttext: Any? = null

)