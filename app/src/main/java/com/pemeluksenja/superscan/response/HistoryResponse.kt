package com.pemeluksenja.superscan.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryResponse(

	@field:SerializedName("history")
	val history: List<HistoryItem>,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class ProductItem(

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("product_price")
	val productPrice: Int,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("product_name")
	val productName: String,

	@field:SerializedName("picture")
	val picture: String
) : Parcelable

@Parcelize
data class HistoryItem(

	@field:SerializedName("product")
	val product: List<ProductItem>,

	@field:SerializedName("paymentCode")
	val paymentCode: String,

	@field:SerializedName("__v")
	val V: Int,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("custName")
	val custName: String,

	@field:SerializedName("orderAt")
	val orderAt: String,

	@field:SerializedName("totalBills")
	val totalBills: Int,

	@field:SerializedName("customer")
	val customer: String
) : Parcelable
