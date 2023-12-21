package com.nccdms.bloomiqpro.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateEmailRequest(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("currentPassword")
	val currentPassword: String? = null
)
