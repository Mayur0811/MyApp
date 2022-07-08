package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class InsuranceNewsResponse(

	@field:SerializedName("response")
	val response: Response?
)

data class Response(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Data(

	@field:SerializedName("InsuranceNewsList")
	val insuranceNewsList: InsuranceNewsList?
)

data class InsuranceNewsList(

	@field:SerializedName("recordcount")
	val recordcount: String?,

	@field:SerializedName("InsuranceNews")
	val insuranceNews: ArrayList<InsuranceNewsItem>
)

data class InsuranceNewsItem(

	@field:SerializedName("news")
	val news: String? = null,

	@field:SerializedName("sr_no")
	val srNo: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("newsdate")
	val newsdate: String? = null
)
