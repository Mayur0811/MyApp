package com.br.stocks.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class EventResponse {
    @SerializedName("scrip_Code")
    @Expose
    var scripCode: String? = null

    @SerializedName("short_name")
    @Expose
    var shortName: String? = null

    @SerializedName("Long_Name")
    @Expose
    var longName: String? = null

    @SerializedName("meeting_date")
    @Expose
    var meetingDate: String? = null

    @SerializedName("URL")
    @Expose
    var url: String? = null
}