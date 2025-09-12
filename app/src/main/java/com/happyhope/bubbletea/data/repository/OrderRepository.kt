package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.model.Order
import com.happyhope.bubbletea.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

/**
 * 주문 데이터 관리를 위한 저장소 인터페이스
 */
interface OrderRepository {
    
    /**
     * 모든 주문 조회 (로컬 데이터베이스에서)
     */
    fun getAllOrders(): Flow<List<Order>>
    
    /**
     * 상태별 주문 조회
     */
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>
    
    /**
     * 특정 주문 조회
     */
    suspend fun getOrderById(id: String): Order?
    
    /**
     * 새 주문 생성
     */
    suspend fun createOrder(order: Order): Result<Order>
    
    /**
     * 주문 상태 업데이트
     */
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Result<Unit>
    
    /**
     * 주문 취소
     */
    suspend fun cancelOrder(orderId: String): Result<Unit>
    
    /**
     * 서버에서 주문 데이터 동기화
     */
    suspend fun syncOrders(userId: String): Result<Unit>
}