package com.nccdms.bloomiqpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class FlowerDetailResponse(

	@field:SerializedName("flower_scientific")
	val flowerScientific: String? = null,

	@field:SerializedName("flower_origin")
	val flowerOrigin: String? = null,

	@field:SerializedName("flower_name")
	val flowerName: String? = null,

	@field:SerializedName("flower_place")
	val flowerPlace: String? = null,

	@field:SerializedName("flower_id")
	val flowerId: Int? = null,

	@field:SerializedName("flower_season")
	val flowerSeason: String? = null,

	@field:SerializedName("flower_image")
	val flowerImage: String? = null,

	@field:SerializedName("flower_description")
	val flowerDescription: String? = null
)
