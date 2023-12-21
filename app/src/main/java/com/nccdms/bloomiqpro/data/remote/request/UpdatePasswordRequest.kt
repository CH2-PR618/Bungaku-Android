package com.nccdms.bloomiqpro.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(

	@field:SerializedName("newPassword")
	val newPassword: String? = null,

	@field:SerializedName("currentPassword")
	val currentPassword: String? = null
)
