package com.happyhope.bubbletea.domain.repository

import com.happyhope.bubbletea.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    
    suspend fun getNews(): Flow<List<News>>
    
    suspend fun refreshNews(): Result<Unit>
    
    suspend fun loadMoreNews(page: Int): Result<Unit>
    
    suspend fun getLocalNews(): List<News>
    
    suspend fun clearOldNews()
}