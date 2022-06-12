package com.pemeluksenja.superscan.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("product")
	val product: List<ProductItems>,

	@field:SerializedName("paymentCode")
	val paymentCode: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("custName")
	val custName: String,

	@field:SerializedName("orderAt")
	val orderAt: String,

	@field:SerializedName("totalBills")
	val totalBills: Int,

	@field:SerializedName("customer")
	val customer: Customer
)

data class ProductItems(

	@field:SerializedName("_id")
	val id: String
)

data class Customer(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("__v")
	val V: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("email")
	val email: String
)
