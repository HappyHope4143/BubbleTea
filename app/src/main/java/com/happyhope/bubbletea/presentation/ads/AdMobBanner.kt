package com.happyhope.bubbletea.presentation.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * AdMob Banner Ad Composable
 * 
 * @param adUnitId The AdMob ad unit ID. Use test ID "ca-app-pub-3940256099942544/6300978111" for testing
 * @param modifier Modifier for the ad view
 */
@Composable
fun AdMobBanner(
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111", // Test ad unit ID
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAdLoaded by remember(adUnitId) { mutableStateOf(false) }
    
    val adView = remember(adUnitId) {
        AdView(context.applicationContext).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
        }
    }
    
    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }
    
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { adView },
        update = { view ->
            if (!isAdLoaded) {
                view.loadAd(AdRequest.Builder().build())
                isAdLoaded = true
            }
        }
    )
}
