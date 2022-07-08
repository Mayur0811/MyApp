package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class StockByCategoryResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("statuscode")
	val statuscode: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<DataItem>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)


