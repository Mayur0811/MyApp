package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @field:SerializedName("created_at")
    val created_at: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("updated_at")
    val updated_at: String,
    @field:SerializedName("post_id")
    val post_id: String? = null
)