package com.adquimo.ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import com.adquimo.core.logging.Logs
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAd(private val context: Context, private val adUnitId: String) {

    private var mRewardedAd: RewardedAd? = null
    private var adListener: AdListener? = null
    private var fullScreenDialog: Dialog? = null
    private var requestId: String? = null

    private var target = 0
    private var hasAdquimoAd = false

    companion object {
        private const val TAG = "TAGD-RewardedAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if Adquimo initialized correctly else adListener.onFailedToLoad
        // TODO: Check Adquimo ad config to request specific ad (Adquimo or AdMob) and set target

        target = 0

        requestId = Logs().requestAd(com.adquimo.core.model.AdRequest(adUnitId, "rewarded"))

        Log.d(TAG, "requested rewarded, requestId $requestId")

        requestAdMob()
    }

    private fun requestAdMob() {
        if (mRewardedAd != null) return

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
                // Log.i(TAG, "onAdLoaded")
                setupAdCallbacks()
                triggerEvent("onAdLoaded")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Log.d(TAG, loadAdError.toString())
                mRewardedAd = null
                triggerEvent("onAdFailedToLoad", message = loadAdError.message)
            }
        })
    }

    private fun setupAdCallbacks() {
        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                // Log.d(TAG, "Ad was clicked.")
                triggerEvent("onAdClicked")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                // Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
                triggerEvent("onAdDismissed")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                // Log.e(TAG, "Ad failed to show fullscreen content. $p0")
                mRewardedAd = null
                triggerEvent("onAdFailedToShow", message = p0.message)
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                // Log.d(TAG, "Ad recorded an impression.")
                triggerEvent("onAdImpression")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                // Log.d(TAG, "Ad showed fullscreen content.")
                triggerEvent("onAdShowed")
            }
        }
    }

    fun showAd() {
        when {
            target == 0 && mRewardedAd != null -> mRewardedAd?.show(context as Activity) { rewardItem ->
                // Log.d(TAG, "User earned the reward.")
                triggerEvent("onAdReward", rewardItem = rewardItem)
            }

            target == 1 -> Log.d(TAG, "Show random")

            hasAdquimoAd -> fullScreenDialog?.show().also { triggerEvent("onAdShowed") }

            else -> Log.d(TAG, "The interstitial ad wasn't ready yet.").also { triggerEvent("onAdFailedToShow", "The interstitial ad wasn't ready yet.") }
        }
    }

    fun isAvailable(): Boolean {
        return (target == 0 && mRewardedAd != null) || hasAdquimoAd;
    }

    fun destroy() {
        mRewardedAd = null
    }

    private fun triggerEvent(kind: String, message: String = "", rewardItem: RewardItem? = null) {
            // TODO: This control via Cache.config.logs.adCallbacks == true
            if (requestId != null) {
                Logs().adCallback(com.adquimo.core.model.AdCallback(requestId!!, kind))
            }

            adListener?.let {
            when (kind) {
                "onAdLoaded" -> it.onAdLoaded()
                "onAdFailedToLoad" -> it.onAdFailedToLoad(message)
                "onAdClicked" -> it.onAdClicked()
                "onAdDismissed" -> it.onAdDismissed()
                "onAdFailedToShow" -> it.onAdFailedToShow(message)
                "onAdImpression" -> it.onAdImpression()
                "onAdShowed" -> it.onAdShowed()
                "onAdReward" -> it.onAdRewardReceived(rewardItem)
            }
        }
    }

    interface Listener {
        fun onAdLoaded() {}
        fun onAdFailedToLoad(errorMessage: String) {}
        fun onAdClicked() {}
        fun onAdDismissed() {}
        fun onAdFailedToShow(errorMessage: String) {}
        fun onAdImpression() {}
        fun onAdShowed() {}
        fun onAdRewardReceived(rewardItem: RewardItem?) {}
    }

    open class AdListener : Listener {
        override fun onAdLoaded() {
            // Default empty behavior
        }

        override fun onAdFailedToLoad(errorMessage: String) {
            // Default empty behavior
        }

        override fun onAdClicked() {
            // Default empty behavior
        }

        override fun onAdDismissed() {
            // Default empty behavior
        }

        override fun onAdFailedToShow(errorMessage: String) {
            // Default empty behavior
        }

        override fun onAdImpression() {
            // Default empty behavior
        }

        override fun onAdShowed() {
            // Default empty behavior
        }

        override fun onAdRewardReceived(rewardItem: RewardItem?) {
            // Default empty behavior
        }
    }
}