package com.adquimo.core.logging

import android.util.Log
import com.adquimo.core.model.AdCallback
import com.adquimo.core.model.AdRequest
import com.adquimo.core.utils.Cache
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class Logs {
    companion object {
        private const val TAG = "TAGD-Logs"
    }

    fun requestAd(adRequest: AdRequest): String? {
        if (Cache.token == null) {
            Log.d(TAG, String.format("Failed to log requestAd with token %s", Cache.token))
            return null
        }

        Log.d(TAG, String.format("Called log requestAd with token %s, %s %s", Cache.token, adRequest.adunit, adRequest.type))

        // Use runBlocking to make the call synchronously
        val response = try {
            runBlocking {
                Cache.restClient.logRequest(Cache.token!!, adRequest)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Network request failed: ${e.message}")
            return null
        }

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val jsonString = responseBody.string()
                val jsonObject = JSONObject(jsonString)
                return jsonObject.getString("requestId")
            } ?: Log.d(TAG, "Failed to log requestAd: Response body is null")
        }

        Log.d(TAG, "Failed to log requestAd: ${response.code()}")
        return null
    }

    fun adCallback(callback: AdCallback): Boolean {
        if (Cache.token == null) {
            Log.d(TAG, String.format("Failed to log adCallback with token %s", Cache.token))
            return false
        }

        Log.d(TAG, String.format("Called log adCallback with token %s, %s %s", Cache.token, callback.requestId, callback.callback))

        // Use runBlocking to make the call synchronously
        val response = try {
            runBlocking {
                Cache.restClient.logCallback(Cache.token!!, callback)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Network request failed: ${e.message}")
            return false
        }

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val jsonString = responseBody.string()
                val jsonObject = JSONObject(jsonString)
                return jsonObject.getString("status").equals("ok")
            } ?: Log.d(TAG, "Failed to log adCallback: Response body is null")
        }

        Log.d(TAG, "Failed to log requestAd: ${response.code()}")
        return false
    }

    fun activity(): Boolean {
        if (Cache.token == null) {
            Log.d(TAG, String.format("Failed to log user activity with token %s", Cache.token))
            return false
        }

        Log.d(TAG, String.format("Called log user activity with token %s", Cache.token))

        // Use runBlocking to make the call synchronously
        val response = try {
            runBlocking {
                Cache.restClient.logActivity(Cache.token!!)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Network request failed: ${e.message}")
            return false
        }

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val jsonString = responseBody.string()
                val jsonObject = JSONObject(jsonString)
                // jsonObject.getString("r") should be assetId

                return true
            } ?: Log.d(TAG, "Failed to log user activity: Response body is null")
        }

        Log.d(TAG, "Failed to log user activity: ${response.code()}")
        return false
    }
}