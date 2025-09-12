package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.local.TeaProductDao
import com.happyhope.bubbletea.data.model.TeaProduct
import com.happyhope.bubbletea.data.network.BubbleTeaApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 티 제품 저장소 구현체
 */
class TeaProductRepositoryImpl(
    private val apiService: BubbleTeaApiService,
    private val localDao: TeaProductDao
) : TeaProductRepository {
    
    override fun getAllProducts(): Flow<List<TeaProduct>> {
        return localDao.getAllProducts()
    }
    
    override fun getProductsByCategory(category: String): Flow<List<TeaProduct>> {
        return localDao.getProductsByCategory(category)
    }
    
    override fun getAvailableProducts(): Flow<List<TeaProduct>> {
        return localDao.getAvailableProducts()
    }
    
    override suspend fun getProductById(id: String): TeaProduct? {
        return withContext(Dispatchers.IO) {
            localDao.getProductById(id)
        }
    }
    
    override suspend fun refreshProducts(): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        localDao.insertProducts(products)
                    }
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to fetch products: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshProductsByCategory(category: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getProductsByCategory(category)
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        localDao.insertProducts(products)
                    }
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to fetch products by category: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}