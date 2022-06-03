package com.pemeluksenja.superscan.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class Result(

	@field:SerializedName("critic")
	val critic: String,

	@field:SerializedName("suggestion")
	val suggestion: String,

	@field:SerializedName("userEmail")
	val userEmail: String,

	@field:SerializedName("userName")
	val userName: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("userId")
	val userId: String
) : Parcelable

@Parcelize
data class User(

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
) : Parcelable
