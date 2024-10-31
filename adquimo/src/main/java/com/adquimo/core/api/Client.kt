package com.adquimo.core.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private const val BASE_URL = "https://api.adquimo.com/sdk/"

    fun getInstance(): Service {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    }
}