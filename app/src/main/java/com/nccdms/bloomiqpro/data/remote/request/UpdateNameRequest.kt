package com.nccdms.bloomiqpro.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateNameRequest(

	@field:SerializedName("name")
	val name: String? = null
)
