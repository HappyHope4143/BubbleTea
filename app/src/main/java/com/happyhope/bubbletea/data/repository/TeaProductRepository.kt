package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.model.TeaProduct
import kotlinx.coroutines.flow.Flow

/**
 * 티 제품 데이터 관리를 위한 저장소 인터페이스
 */
interface TeaProductRepository {
    
    /**
     * 모든 제품 조회 (로컬 데이터베이스에서)
     */
    fun getAllProducts(): Flow<List<TeaProduct>>
    
    /**
     * 카테고리별 제품 조회
     */
    fun getProductsByCategory(category: String): Flow<List<TeaProduct>>
    
    /**
     * 사용 가능한 제품 조회
     */
    fun getAvailableProducts(): Flow<List<TeaProduct>>
    
    /**
     * 특정 제품 조회
     */
    suspend fun getProductById(id: String): TeaProduct?
    
    /**
     * 서버에서 제품 데이터 새로고침
     */
    suspend fun refreshProducts(): Result<Unit>
    
    /**
     * 특정 카테고리 제품 새로고침
     */
    suspend fun refreshProductsByCategory(category: String): Result<Unit>
}