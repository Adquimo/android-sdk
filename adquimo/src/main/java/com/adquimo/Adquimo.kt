package com.adquimo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.adquimo.core.api.Client
import com.adquimo.core.repository.ConfigRepository
import com.adquimo.core.device.HDevice
import com.adquimo.core.logging.Logs
import com.adquimo.core.model.Device
import com.adquimo.core.utils.Cache
import org.json.JSONObject

class Adquimo {

    companion object {
        private const val TAG = "TAGD-Adquimo"

        @JvmStatic
        suspend fun initialize(context: Context, appId: String) {
            // Logic for initialization goes here
            Log.d(TAG, "Adquimo initialized with appId: $appId and context: $context")

            // TODO: Fetch sdk config with appId
            // TODO: Check database sqlite if device not registered
            Cache.initialize(context)

            val deviceId = Cache.configRepository.getDeviceId()

            if (deviceId == null) {

                Log.d(TAG, "deviceId is null or not set, setting..")

                // Proceed with device info extraction
                val device = HDevice(context)

                /* CoroutineScope(Dispatchers.IO).launch {
                               register(device.metadata())
                           } */
                register(context, appId, device.metadata())
                return
            }

            Cache.token = deviceId
            Log.d(TAG, "deviceId is already set: $deviceId")

            // Trigger log user active to generate stats of mau, dau etc
            Logs().activity()
        }

        private suspend fun register(context: Context, appId: String, device: Device) {
            val response = Cache.restClient.registerDevice(context.packageName, device)
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    // Convert ResponseBody to String
                    val jsonString = responseBody.string()

                    // Parse the JSON string using JSONObject
                    val jsonObject = JSONObject(jsonString)

                    // Access fields directly, e.g., deviceId
                    val deviceId = jsonObject.getString("deviceId")

                    Log.d(TAG, "Device set deviceId: $deviceId")

                    Cache.token = String.format("%s_%s", appId, deviceId)
                    Cache.configRepository.setDeviceId(String.format("%s_%s", appId, deviceId))
                } ?: Log.d(TAG, "Failed to register device: Response body is null")
            } else {
                Log.d(TAG, "Failed to register device: ${response.code()}")
            }
        }
    }
}