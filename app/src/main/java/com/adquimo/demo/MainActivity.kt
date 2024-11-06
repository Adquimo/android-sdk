package com.adquimo.demo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adquimo.Adquimo
import com.adquimo.ads.AppOpenAd
import com.adquimo.ads.InterstitialAd
import com.adquimo.ads.RewardedAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appOpenAd: AppOpenAd
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var rewardedAd: RewardedAd

    companion object {
        private const val TAG = "TAGD-MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btAppOpen: Button = findViewById(R.id.ad_app_open)
        val btInterstitial: Button = findViewById(R.id.ad_interstitial)
        val btRewarded: Button = findViewById(R.id.ad_rewarded)

        // Adquimo.initialize(this, "ADQUIMO_APP_ID")

        // Set up the click listener
        btAppOpen.setOnClickListener { requestAndShowAppOpen() }
        btInterstitial.setOnClickListener { requestAndShowInterstitial() }
        btRewarded.setOnClickListener { requestAndShowRewarded() }

        init();
    }

    private fun init() {
        Adquimo.initialize(this@MainActivity, "b506cf57-65f3-4feb-bdf0-66e7ca1ef16f")

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {}
        }
    }

    private fun requestAndShowAppOpen() {
        appOpenAd = AppOpenAd(this, "ca-app-pub-3940256099942544/9257395921", false)

        appOpenAd.setAdListener(object : AppOpenAd.AdListener {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                appOpenAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }
        })

        appOpenAd.loadAd()
    }

    private fun requestAndShowInterstitial() {
        interstitialAd = InterstitialAd(this, "ca-app-pub-3940256099942544/1033173712")

        interstitialAd.setAdListener(object : InterstitialAd.AdListener {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                interstitialAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }
        })

        interstitialAd.loadAd()
    }

    private fun requestAndShowRewarded() {
        rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")

        rewardedAd.setAdListener(object : RewardedAd.AdListener {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                rewardedAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }

            override fun onAdRewardReceived(rewardItem: RewardItem?) {
                super.onAdRewardReceived(rewardItem)
                Log.d(TAG, "Ad Reward Received $rewardItem")
            }
        })

        rewardedAd.loadAd()
    }

}
