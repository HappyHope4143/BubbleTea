package com.happyhope.bubbletea

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BubbleTeaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this) {}
    }
}