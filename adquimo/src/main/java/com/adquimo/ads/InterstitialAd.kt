package com.adquimo.ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.ImageButton
import com.adquimo.Adquimo
import com.adquimo.Adquimo.Companion
import com.adquimo.R
import com.adquimo.core.logging.Logs
import com.adquimo.core.model.Device
import com.adquimo.core.utils.Cache
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.json.JSONObject

class InterstitialAd(private val context: Context, private val adUnitId: String) {

    private var mInterstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd? = null
    private var adListener: AdListener? = null
    private var fullScreenDialog: Dialog? = null
    private var requestId: String? = null

    private var target = 0
    private var hasAdquimoAd = false

    companion object {
        private const val TAG = "TAGD-InterstitialAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if Adquimo initialized correctly else adListener.onFailedToLoad
        // TODO: Check Adquimo ad config to request specific ad (Adquimo or AdMob) and set target

        target = 0

        requestId = Logs().requestAd(com.adquimo.core.model.AdRequest(adUnitId, "interstitial"))

        Log.d(TAG, "requested interstitial, requestId $requestId")

        // requestAdquimo()
        requestAdMob()
    }

    private fun requestAdquimo() {
        // TODO: Trigger request ad

        hasAdquimoAd = true

        fullScreenDialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            setContentView(R.layout.ad_interstitial)
            setCancelable(false)

            findViewById<ImageButton>(R.id.close).setOnClickListener {
                fullScreenDialog?.takeIf { it.isShowing }?.dismiss().also {
                    triggerEvent("onAdDismissed")
                }
            }
        }

        // Set up ad content, video/image, redirect, etc.
        triggerEvent("onAdLoaded")
    }

    private fun requestAdMob() {
        if (mInterstitialAd != null) return

        val adRequest = AdRequest.Builder().build()
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                mInterstitialAd = interstitialAd
                Log.i(TAG, "onAdLoaded")
                setupAdCallbacks()
                triggerEvent("onAdLoaded")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(TAG, loadAdError.toString())
                mInterstitialAd = null
                triggerEvent("onAdFailedToShow", loadAdError.message)
            }
        })
    }

    private fun setupAdCallbacks() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() = triggerEvent("onAdClicked")
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
                triggerEvent("onAdDismissed")
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
                triggerEvent("onAdFailedToShow", adError.message)
            }
            override fun onAdImpression() = triggerEvent("onAdImpression")
            override fun onAdShowedFullScreenContent() = triggerEvent("onAdShowed")
        }
    }

    fun showAd() {
        when {
            target == 0 && mInterstitialAd != null -> mInterstitialAd?.show(context as Activity)
            target == 1 -> Log.d(TAG, "Show random")
            hasAdquimoAd -> fullScreenDialog?.show().also { triggerEvent("onAdShowed") }
            else -> Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
    }

    private fun triggerEvent(kind: String, message: String = "") {
            // TODO: This control via Cache.config.logs.adCallbacks == true
            /* if (requestId != null) {
                Logs().adCallback(com.adquimo.core.model.AdCallback(requestId!!, kind))
            } */

            adListener?.let {
            when (kind) {
                "onAdLoaded" -> it.onAdLoaded()
                "onAdFailedToLoad" -> it.onAdFailedToLoad(message)
                "onAdClicked" -> it.onAdClicked()
                "onAdDismissed" -> it.onAdDismissed()
                "onAdFailedToShow" -> it.onAdFailedToShow(message)
                "onAdImpression" -> it.onAdImpression()
                "onAdShowed" -> it.onAdShowed()
            }
        }
    }

    interface AdListener {
        fun onAdLoaded() {}
        fun onAdFailedToLoad(errorMessage: String) {}
        fun onAdClicked() {}
        fun onAdDismissed() {}
        fun onAdFailedToShow(errorMessage: String) {}
        fun onAdImpression() {}
        fun onAdShowed() {}
    }
}