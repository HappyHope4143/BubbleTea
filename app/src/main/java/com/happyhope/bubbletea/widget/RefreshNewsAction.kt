package com.happyhope.bubbletea.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import androidx.room.Room
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
import com.happyhope.bubbletea.data.local.SampleNewsProvider
import com.happyhope.bubbletea.data.mapper.NewsDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Action callback for refreshing news in the widget
 */
class RefreshNewsAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Refresh news data
        refreshNewsData(context)
        
        // Update all widgets
        NewsWidget().updateAll(context)
    }
    
    private suspend fun refreshNewsData(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    BubbleTeaDatabase::class.java,
                    BubbleTeaDatabase.DATABASE_NAME
                ).build()
                
                val newsDao = database.newsDao()
                val sampleNewsProvider = SampleNewsProvider()
                
                // For now, use sample news provider since we may not have API key configured
                // In production, this would use the actual NewsRepository with API call
                val sampleNews = sampleNewsProvider.getSampleNews()
                
                // Clear old news and insert new ones
                newsDao.deleteAllNews()
                newsDao.insertNews(sampleNews)
            } catch (e: Exception) {
                // Silent fail - widget will show cached data
                e.printStackTrace()
            }
        }
    }
}
