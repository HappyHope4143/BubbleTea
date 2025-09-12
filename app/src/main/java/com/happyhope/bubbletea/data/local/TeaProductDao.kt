package com.happyhope.bubbletea.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.happyhope.bubbletea.data.model.TeaProduct
import kotlinx.coroutines.flow.Flow

/**
 * 티 제품 데이터 접근 객체
 */
@Dao
interface TeaProductDao {
    
    @Query("SELECT * FROM tea_products")
    fun getAllProducts(): Flow<List<TeaProduct>>
    
    @Query("SELECT * FROM tea_products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<TeaProduct>>
    
    @Query("SELECT * FROM tea_products WHERE id = :id")
    suspend fun getProductById(id: String): TeaProduct?
    
    @Query("SELECT * FROM tea_products WHERE isAvailable = 1")
    fun getAvailableProducts(): Flow<List<TeaProduct>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: TeaProduct)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<TeaProduct>)
    
    @Update
    suspend fun updateProduct(product: TeaProduct)
    
    @Delete
    suspend fun deleteProduct(product: TeaProduct)
    
    @Query("DELETE FROM tea_products")
    suspend fun deleteAllProducts()
}