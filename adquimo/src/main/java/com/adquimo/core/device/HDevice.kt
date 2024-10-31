package com.adquimo.core.device

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import com.adquimo.core.model.Device
import java.io.File
import java.util.Locale
import java.util.TimeZone

class HDevice(private val context: Context) {

    companion object {
        private const val TAG = "TAGD-HDevice"
    }

    fun metadata(): Device {
        val timezoneId = TimeZone.getDefault().id
        val lang = Locale.getDefault().language
        val model = Build.MODEL
        val os = "${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"
        val root = isDeviceRooted()
        val netType = getNetworkType()
        val carrier = getCarrier()

        return Device(
            timezoneId = timezoneId,
            lang = lang,
            model = model,
            os = os,
            root = root,
            netType = netType,
            carrier = carrier
        )
    }

    private fun isDeviceRooted(): Boolean {
        // Simple check for rooted device
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun getNetworkType(): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return when (activeNetwork?.type) {
            ConnectivityManager.TYPE_WIFI -> "WiFi"
            ConnectivityManager.TYPE_MOBILE -> "Mobile"
            else -> "No Connection"
        }
    }

    private fun getCarrier(): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperatorName ?: "Unknown Carrier"
    }
}
