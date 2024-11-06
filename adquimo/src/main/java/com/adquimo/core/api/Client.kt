package com.adquimo.core.api

import com.adquimo.core.utils.Library
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private const val BASE_URL = "https://api.adquimo.com/sdk/"

    fun getInstance(): Service {
        val client = OkHttpClient.Builder()
            .addInterceptor(VersionInterceptor())  // Add the version interceptor
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    }
}

class VersionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("aq-version", Library.VERSION)
            .build()
        return chain.proceed(request)
    }
}