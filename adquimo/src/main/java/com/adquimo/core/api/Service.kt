package com.adquimo.core.api

import com.adquimo.core.model.AdCallback
import com.adquimo.core.model.AdRequest
import com.adquimo.core.model.Device
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Service {
    // Device
    @POST("device/register")
    suspend fun registerDevice(
        @Header("app-id") appId: String,
        @Body device: Device
    ): Response<ResponseBody>


    // Logs Ad
    @POST("logs/ad/request")
    suspend fun logRequest(
        @Header("aq-token") token: String,
        @Body adRequest: AdRequest
    ): Response<ResponseBody>

    @POST("logs/ad/callback")
    suspend fun logCallback(
        @Header("aq-token") token: String,
        @Body callback: AdCallback
    ): Response<ResponseBody>


    // Logs User
    @POST("logs/user/activity")
    suspend fun logActivity(
        @Header("aq-token") token: String,
    ): Response<ResponseBody>
}