package com.adquimo.demo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adquimo.Adquimo
import com.adquimo.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var interstitialAd: InterstitialAd

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

        val myButton: Button = findViewById(R.id.button)

        // Adquimo.initialize(this, "ADQUIMO_APP_ID")

        // Set up the click listener
        myButton.setOnClickListener { requestAndShow() }

        init();
    }

    private fun init() {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {}
            Adquimo.initialize(this@MainActivity, "b506cf57-65f3-4feb-bdf0-66e7ca1ef16f")
        }
    }

    private fun requestAndShow() {
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
}
