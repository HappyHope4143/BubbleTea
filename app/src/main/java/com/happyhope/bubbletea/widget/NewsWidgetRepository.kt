package com.happyhope.bubbletea.widget

import android.content.Context
import androidx.room.Room
import com.happyhope.bubbletea.BuildConfig
import com.happyhope.bubbletea.data.api.service.NewsApiService
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
import com.happyhope.bubbletea.data.mapper.NewsDataMapper
import com.happyhope.bubbletea.domain.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Repository for widget to access news data
 * Uses singleton pattern to avoid multiple database instances
 */
class NewsWidgetRepository private constructor(context: Context) {
    
    private val database = Room.databaseBuilder(
        context.applicationContext,
        BubbleTeaDatabase::class.java,
        BubbleTeaDatabase.DATABASE_NAME
    ).build()
    
    private val newsDao = database.newsDao()
    private val mapper = NewsDataMapper()
    
    // Lazy initialization of API service
    private val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
    
    fun getLocalNews(): List<News> = runBlocking(Dispatchers.IO) {
        try {
            newsDao.getNewsWithLimit(10).map { mapper.mapEntityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun refreshNews(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = newsApiService.getTopHeadlines(
                category = "technology",
                country = "us",
                pageSize = 20,
                apiKey = BuildConfig.NEWS_API_KEY
            )
            
            if (response.status == "ok" && response.articles.isNotEmpty()) {
                val newsEntities = mapper.mapApiListToEntityList(response.articles)
                
                if (newsEntities.isNotEmpty()) {
                    // Get existing URLs to avoid duplicates
                    val existingUrls = newsDao.getAllUrls().toSet()
                    
                    // Filter out duplicates by URL
                    val newArticles = newsEntities.distinctBy { it.url }
                        .filter { it.url !in existingUrls }
                    
                    // Insert new articles
                    if (newArticles.isNotEmpty()) {
                        newsDao.insertNews(newArticles)
                    }
                    
                    // Keep only the latest N articles using same logic as main repository
                    val currentCount = newsDao.getNewsCount()
                    if (currentCount > BubbleTeaDatabase.NEWS_LIMIT) {
                        val excessCount = currentCount - BubbleTeaDatabase.NEWS_LIMIT
                        newsDao.deleteOldestNews(excessCount)
                    }
                    
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("No valid articles"))
                }
            } else {
                Result.failure(Exception("API error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: NewsWidgetRepository? = null
        
        fun getInstance(context: Context): NewsWidgetRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsWidgetRepository(context).also { INSTANCE = it }
            }
        }
    }
}
