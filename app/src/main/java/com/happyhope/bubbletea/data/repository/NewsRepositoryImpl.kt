package com.happyhope.bubbletea.data.repository

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
    
    // This would typically come from BuildConfig or a configuration file
    private val apiKey = "demo-api-key" // In real app, use secure storage
    
    override suspend fun getNews(): Flow<List<News>> {
        return flow {
            // First check local data
            val localNews = getLocalNews()
            if (localNews.isNotEmpty()) {
                emit(localNews)
            } else {
                // If no local data, add sample data for demonstration
                val sampleNews = sampleNewsProvider.getSampleNews()
                newsDao.insertNews(sampleNews)
                emit(mapper.mapEntitiesToDomain(sampleNews))
            }
            
            // Try to refresh from network (will fail with demo API key)
            try {
                refreshNews()
            } catch (e: Exception) {
                // Continue with cached/sample data if network fails
                // This is expected behavior for the demo
            }
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun refreshNews(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // This will likely fail with demo API key, but that's expected
                val response = newsApiService.getNews(apiKey = apiKey)
                val newsEntities = mapper.mapApiListToEntityList(response.articles)
                
                if (newsEntities.isNotEmpty()) {
                    newsDao.insertNews(newsEntities)
                    clearOldNews()
                    Result.success(Unit)
                } else {
                    // Fallback to sample data if API returns empty
                    val sampleNews = sampleNewsProvider.getSampleNews()
                    newsDao.insertNews(sampleNews)
                    clearOldNews()
                    Result.success(Unit)
                }
            } catch (e: Exception) {
                // If API fails, ensure we have sample data
                val currentCount = newsDao.getNewsCount()
                if (currentCount == 0) {
                    val sampleNews = sampleNewsProvider.getSampleNews()
                    newsDao.insertNews(sampleNews)
                }
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