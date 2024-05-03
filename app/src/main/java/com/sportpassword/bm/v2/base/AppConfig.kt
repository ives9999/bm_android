package com.sportpassword.bm.v2.base

import com.google.gson.GsonBuilder
import com.sportpassword.bm.Utilities.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfig {

    // create retrofit service
    fun ApiService2(): ApiService2 =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ApiService2::class.java)
}