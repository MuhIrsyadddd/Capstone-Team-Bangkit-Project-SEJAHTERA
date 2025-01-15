package com.example.capstonesejahtera

import com.google.gson.annotations.SerializedName

data class EmasResponse(

	@field:SerializedName("predicted_prices")
	val predictedPrices: List<Any?>? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
