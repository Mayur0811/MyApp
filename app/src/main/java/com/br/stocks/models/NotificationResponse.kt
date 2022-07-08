package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @field:SerializedName("data")
    val data: List<NotificationData>,
    @field:SerializedName("msg")
    val msg: String,
    @field:SerializedName("status")
    val status: Int,
    @field:SerializedName("statuscode")
    val statuscode: Int
)