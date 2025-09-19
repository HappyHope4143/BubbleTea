package com.happyhope.bubbletea.data.local

import com.happyhope.bubbletea.data.entity.NewsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleNewsProvider @Inject constructor() {
    
    fun getSampleNews(): List<NewsEntity> {
        val currentTime = System.currentTimeMillis()
        
        return listOf(
            NewsEntity(
                title = "BubbleTea App Data Layer Implementation Complete",
                summary = "Successfully implemented Room database, Retrofit API integration, and Repository pattern for news data management with offline-first approach.",
                url = "https://github.com/HappyHope4143/BubbleTea",
                createdAt = currentTime - 300000 // 5 minutes ago
            ),
            NewsEntity(
                title = "Modern Android Architecture with Hilt",
                summary = "Using dependency injection with Hilt to create a clean, testable architecture following MVVM pattern and clean architecture principles.",
                url = "https://developer.android.com/training/dependency-injection/hilt-android",
                createdAt = currentTime - 600000 // 10 minutes ago
            ),
            NewsEntity(
                title = "Jetpack Compose News Reader Features",
                summary = "Building a responsive news reader with Compose UI components, navigation, and state management for better user experience.",
                url = "https://developer.android.com/jetpack/compose",
                createdAt = currentTime - 900000 // 15 minutes ago
            ),
            NewsEntity(
                title = "Room Database for Local Storage",
                summary = "Implementing local data persistence with Room database including DAO patterns, entity definitions, and automatic old data cleanup.",
                url = "https://developer.android.com/training/data-storage/room",
                createdAt = currentTime - 1200000 // 20 minutes ago
            ),
            NewsEntity(
                title = "Retrofit API Integration Best Practices",
                summary = "Setting up network layer with Retrofit, OkHttp interceptors, and proper error handling for robust API communication.",
                url = "https://square.github.io/retrofit/",
                createdAt = currentTime - 1500000 // 25 minutes ago
            )
        )
    }
}