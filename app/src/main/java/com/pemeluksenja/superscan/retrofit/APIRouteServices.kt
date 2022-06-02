package com.pemeluksenja.superscan.retrofit

import com.pemeluksenja.superscan.model.Login
import com.pemeluksenja.superscan.model.Rate
import com.pemeluksenja.superscan.model.Register
import com.pemeluksenja.superscan.response.LoginResponse
import com.pemeluksenja.superscan.response.RateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIRouteServices {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body login: Login): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body register: Register): Call<Register>

    @Headers("Content-Type: application/json")
    @POST("rate")
    fun rate(@Header("Authorization") Token: String, @Body rate: Rate): Call<RateResponse>


}