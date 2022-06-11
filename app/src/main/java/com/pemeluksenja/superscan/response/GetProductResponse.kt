package com.pemeluksenja.superscan.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetProductResponse(

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("product_price")
	val productPrice: Int,

	@field:SerializedName("product_name")
	val productName: String
) : Parcelable
