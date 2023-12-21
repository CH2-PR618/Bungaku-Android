package com.nccdms.bloomiqpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("msg")
	val msg: String? = null
)
