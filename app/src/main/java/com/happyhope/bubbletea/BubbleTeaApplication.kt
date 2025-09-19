package com.happyhope.bubbletea

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BubbleTeaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}