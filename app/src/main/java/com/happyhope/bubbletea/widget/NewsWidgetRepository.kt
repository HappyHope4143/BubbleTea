package com.happyhope.bubbletea.widget

import android.content.Context
import androidx.room.Room
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
import com.happyhope.bubbletea.data.mapper.NewsDataMapper
import com.happyhope.bubbletea.domain.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

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
    
    fun getLocalNews(): List<News> = runBlocking(Dispatchers.IO) {
        try {
            newsDao.getNewsWithLimit(10).map { mapper.mapEntityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
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
