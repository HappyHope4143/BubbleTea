package com.happyhope.bubbletea.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.happyhope.bubbletea.data.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    
    @Query("SELECT * FROM news ORDER BY created_at DESC")
    fun getAllNews(): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news ORDER BY created_at DESC LIMIT :limit")
    suspend fun getNewsWithLimit(limit: Int): List<NewsEntity>
    
    @Query("SELECT url FROM news")
    suspend fun getAllUrls(): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsItems: List<NewsEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNews(news: NewsEntity): Long
    
    @Query("DELETE FROM news WHERE id IN (SELECT id FROM news ORDER BY created_at ASC LIMIT :count)")
    suspend fun deleteOldestNews(count: Int)
    
    @Query("SELECT COUNT(*) FROM news")
    suspend fun getNewsCount(): Int
    
    @Query("DELETE FROM news")
    suspend fun deleteAllNews()
    
    @Query("SELECT * FROM news WHERE url = :url LIMIT 1")
    suspend fun getNewsByUrl(url: String): NewsEntity?
    
    /**
     * Keep only the latest N news items, deleting older ones.
     * @param limit Maximum number of news items to keep
     */
    suspend fun keepLatestOnly(limit: Int) {
        val currentCount = getNewsCount()
        if (currentCount > limit) {
            val excessCount = currentCount - limit
            deleteOldestNews(excessCount)
        }
    }
}