package com.happyhope.bubbletea.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "news",
    indices = [Index(value = ["url"], unique = true)]
)
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "summary")
    val summary: String,
    
    @ColumnInfo(name = "url")
    val url: String,
    
    @ColumnInfo(name = "source")
    val source: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)