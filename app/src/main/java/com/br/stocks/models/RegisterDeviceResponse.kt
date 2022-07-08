package com.br.stocks.models

import com.google.gson.annotations.SerializedName

data class RegisterDeviceResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("statuscode")
	val statuscode: Int? = null,

	@field:SerializedName("device")
	val device: Device? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Device(

	@field:SerializedName("device_id")
	val deviceId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("device_token")
	val deviceToken: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("device_type")
	val deviceType: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
