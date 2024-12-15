package com.example.capstonesejahtera

import com.google.gson.annotations.SerializedName

data class SahamResponse(

	@field:SerializedName("1_year_prediction_date")
	val jsonMember1YearPredictionDate: JsonMember1YearPredictionDate? = null,

	@field:SerializedName("1_month_prediction_date")
	val jsonMember1MonthPredictionDate: JsonMember1MonthPredictionDate? = null,

	@field:SerializedName("1_day_prediction_date")
	val jsonMember1DayPredictionDate: JsonMember1DayPredictionDate? = null
)

data class JsonMember1MonthPredictionDate(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("predicted_price")
	val predictedPrice: Any? = null
)

data class JsonMember1YearPredictionDate(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("predicted_price")
	val predictedPrice: Any? = null
)

data class JsonMember1DayPredictionDate(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("predicted_price")
	val predictedPrice: Any? = null
)
