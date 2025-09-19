package com.happyhope.bubbletea.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.happyhope.bubbletea.data.dao.NewsDao
import com.happyhope.bubbletea.data.entity.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BubbleTeaDatabase : RoomDatabase() {
    
    abstract fun newsDao(): NewsDao
    
    companion object {
        const val DATABASE_NAME = "bubbletea_database"
        const val NEWS_LIMIT = 100 // Maximum number of news items to keep
    }
}