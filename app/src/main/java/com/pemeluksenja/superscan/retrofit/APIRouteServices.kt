package com.pemeluksenja.superscan.retrofit

import com.pemeluksenja.superscan.model.Login
import com.pemeluksenja.superscan.model.Order
import com.pemeluksenja.superscan.model.Rate
import com.pemeluksenja.superscan.model.Register
import com.pemeluksenja.superscan.response.*
import retrofit2.Call
import retrofit2.http.*

interface APIRouteServices {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body login: Login): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body register: Register): Call<Register>

    @Headers("Content-Type: application/json")
    @POST("order")
    fun order(@Header("Authorization") Token: String, @Body order: Order): Call<OrderResponse>


    @Headers("Content-Type: application/json")
    @POST("rate")
    fun rate(@Header("Authorization") Token: String, @Body rate: Rate): Call<RateResponse>

    @GET("getProduct/{product_name}")
    fun getProduct(@Path ("product_name") product_name: String): Call<GetProductResponse>

    @GET("history/{id}")
    fun getHistory(@Header("Authorization") Token: String, @Path("id") id: String): Call<HistoryResponse>
}