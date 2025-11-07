package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.BuildConfig
import com.happyhope.bubbletea.data.api.service.NewsApiService
import com.happyhope.bubbletea.data.dao.NewsDao
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
import com.happyhope.bubbletea.data.local.SampleNewsProvider
import com.happyhope.bubbletea.data.mapper.NewsDataMapper
import com.happyhope.bubbletea.domain.model.News
import com.happyhope.bubbletea.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
    private val mapper: NewsDataMapper,
    private val sampleNewsProvider: SampleNewsProvider
) : NewsRepository {
    
    override suspend fun getNews(): Flow<List<News>> {
        return flow {
            // First emit cached data if available
            val localNews = getLocalNews()
            if (localNews.isNotEmpty()) {
                emit(localNews)
            }
            
            // Then try to refresh from network
            val refreshResult = refreshNews()
            if (refreshResult.isSuccess) {
                // Emit fresh data after successful refresh
                val updatedNews = getLocalNews()
                if (updatedNews.isNotEmpty()) {
                    emit(updatedNews)
                }
            } else if (localNews.isEmpty()) {
                // Only use sample data if no cached data and network failed
                val sampleNews = sampleNewsProvider.getSampleNews()
                newsDao.insertNews(sampleNews)
                emit(mapper.mapEntitiesToDomain(sampleNews))
            }
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun refreshNews(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Try to fetch technology news (IT/Science related)
                val response = newsApiService.getTopHeadlines(
                    category = "technology",
                    country = "us",
                    pageSize = 20,
                    apiKey = BuildConfig.NEWS_API_KEY
                )
                
                // Check if API returned valid data
                if (response.status == "ok" && response.articles.isNotEmpty()) {
                    val newsEntities = mapper.mapApiListToEntityList(response.articles)
                    
                    if (newsEntities.isNotEmpty()) {
                        // Get existing URLs to avoid unnecessary database operations
                        val existingUrls = newsDao.getAllUrls().toSet()
                        
                        // Filter out duplicates by URL (only insert new articles)
                        val newArticles = newsEntities.distinctBy { it.url }
                            .filter { it.url !in existingUrls }
                        
                        // Insert new articles
                        if (newArticles.isNotEmpty()) {
                            newsDao.insertNews(newArticles)
                        }
                        
                        // Keep only the latest N articles
                        clearOldNews()
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("No valid articles in response"))
                    }
                } else {
                    Result.failure(Exception("API returned error status or empty articles"))
                }
            } catch (e: Exception) {
                // Log error and return failure
                // In production, you might want to use a proper logging framework
                println("NewsAPI Error: ${e.message}")
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getLocalNews(): List<News> {
        return withContext(Dispatchers.IO) {
            val entities = newsDao.getNewsWithLimit(BubbleTeaDatabase.NEWS_LIMIT)
            mapper.mapEntitiesToDomain(entities)
        }
    }
    
    override suspend fun clearOldNews() {
        withContext(Dispatchers.IO) {
            val currentCount = newsDao.getNewsCount()
            if (currentCount > BubbleTeaDatabase.NEWS_LIMIT) {
                val excessCount = currentCount - BubbleTeaDatabase.NEWS_LIMIT
                newsDao.deleteOldestNews(excessCount)
            }
        }
    }
}