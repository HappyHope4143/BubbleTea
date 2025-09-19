package com.happyhope.bubbletea.di

import android.content.Context
import androidx.room.Room
import com.happyhope.bubbletea.data.dao.NewsDao
import com.happyhope.bubbletea.data.database.BubbleTeaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BubbleTeaDatabase {
        return Room.databaseBuilder(
            context,
            BubbleTeaDatabase::class.java,
            BubbleTeaDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideNewsDao(database: BubbleTeaDatabase): NewsDao {
        return database.newsDao()
    }
}