package com.pemeluksenja.superscan.retrofit

import com.pemeluksenja.superscan.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfiguration {
    companion object {
        fun getAPIServices(): APIRouteServices {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder().baseUrl("https://superscan-352006.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            return retrofit.create(APIRouteServices::class.java)
        }
    }
}