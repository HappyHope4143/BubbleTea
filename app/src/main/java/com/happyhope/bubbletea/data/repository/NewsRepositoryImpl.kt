package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.api.service.NewsApiService
import com.happyhope.bubbletea.data.dao.NewsDao
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
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
    private val mapper: NewsDataMapper
) : NewsRepository {
    
    // This would typically come from BuildConfig or a configuration file
    private val apiKey = "demo-api-key" // In real app, use secure storage
    
    override suspend fun getNews(): Flow<List<News>> {
        return flow {
            // Try local first
            val localNews = newsDao.getAllNews()
            localNews.collect { entities ->
                if (entities.isNotEmpty()) {
                    emit(mapper.mapEntitiesToDomain(entities))
                }
            }
            
            // Try to refresh from network
            try {
                refreshNews()
            } catch (e: Exception) {
                // Continue with cached data if network fails
                // Local data is already emitted above
            }
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun refreshNews(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = newsApiService.getNews(apiKey = apiKey)
                val newsEntities = mapper.mapApiListToEntityList(response.articles)
                
                if (newsEntities.isNotEmpty()) {
                    newsDao.insertNews(newsEntities)
                    clearOldNews()
                }
                
                Result.success(Unit)
            } catch (e: Exception) {
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