package com.happyhope.bubbletea.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.happyhope.bubbletea.data.model.Order
import com.happyhope.bubbletea.data.model.TeaProduct

/**
 * 버블티 앱 로컬 데이터베이스
 */
@Database(
    entities = [TeaProduct::class, Order::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BubbleTeaDatabase : RoomDatabase() {
    
    abstract fun teaProductDao(): TeaProductDao
    abstract fun orderDao(): OrderDao
    
    companion object {
        @Volatile
        private var INSTANCE: BubbleTeaDatabase? = null
        
        fun getDatabase(context: Context): BubbleTeaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BubbleTeaDatabase::class.java,
                    "bubble_tea_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}