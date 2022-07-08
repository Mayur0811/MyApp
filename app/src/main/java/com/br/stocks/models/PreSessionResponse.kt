package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class PreSessionResponse(
    @field:SerializedName("response")
    val response: ResponsePre?
)

data class ResponsePre(
    @field:SerializedName("data")
    val data: DataNewsList?,
    @field:SerializedName("type")
    val type: String?
)

data class DataNewsList(
    @field:SerializedName("newslist")
    val newsList: NewsList?
)

data class NewsList(
    @field:SerializedName("news")
    val news: List<New>,
    @field:SerializedName("recordcount")
    val recordCount: String?
)

data class New(
    @field:SerializedName("arttext")
    val artText: Any?,
    @field:SerializedName("caption")
    val caption: Any?,
    @field:SerializedName("co_code")
    val co_code: Any?,
    @field:SerializedName("date")
    val date: String?,
    @field:SerializedName("flag")
    val flag: String?,
    @field:SerializedName("heading")
    val heading: String?,
    @field:SerializedName("ind_code")
    val ind_code: Any?,
    @field:SerializedName("section_name")
    val section_name: String?,
    @field:SerializedName("sno")
    val sno: String?,
    @field:SerializedName("time")
    val time: String?
)