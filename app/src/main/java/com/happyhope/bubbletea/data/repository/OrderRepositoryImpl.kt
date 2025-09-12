package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.local.OrderDao
import com.happyhope.bubbletea.data.model.Order
import com.happyhope.bubbletea.data.model.OrderStatus
import com.happyhope.bubbletea.data.network.BubbleTeaApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 주문 저장소 구현체
 */
class OrderRepositoryImpl(
    private val apiService: BubbleTeaApiService,
    private val localDao: OrderDao
) : OrderRepository {
    
    override fun getAllOrders(): Flow<List<Order>> {
        return localDao.getAllOrders()
    }
    
    override fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> {
        return localDao.getOrdersByStatus(status)
    }
    
    override suspend fun getOrderById(id: String): Order? {
        return withContext(Dispatchers.IO) {
            localDao.getOrderById(id)
        }
    }
    
    override suspend fun createOrder(order: Order): Result<Order> {
        return try {
            withContext(Dispatchers.IO) {
                // 로컬에 주문 저장
                localDao.insertOrder(order)
                
                // 서버에 주문 전송
                val response = apiService.createOrder(order)
                if (response.isSuccessful) {
                    response.body()?.let { serverOrder ->
                        // 서버에서 받은 주문 정보로 로컬 업데이트
                        localDao.insertOrder(serverOrder)
                        Result.success(serverOrder)
                    } ?: Result.success(order)
                } else {
                    Result.failure(Exception("Failed to create order: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val order = localDao.getOrderById(orderId)
                if (order != null) {
                    val updatedOrder = order.copy(status = status)
                    localDao.updateOrder(updatedOrder)
                    
                    // 서버에도 업데이트 전송
                    val response = apiService.updateOrderStatus(orderId, updatedOrder)
                    if (response.isSuccessful) {
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Failed to update order status: ${response.code()}"))
                    }
                } else {
                    Result.failure(Exception("Order not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun cancelOrder(orderId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val order = localDao.getOrderById(orderId)
                if (order != null) {
                    val cancelledOrder = order.copy(status = OrderStatus.CANCELLED)
                    localDao.updateOrder(cancelledOrder)
                    
                    // 서버에서 주문 취소
                    val response = apiService.cancelOrder(orderId)
                    if (response.isSuccessful) {
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Failed to cancel order: ${response.code()}"))
                    }
                } else {
                    Result.failure(Exception("Order not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncOrders(userId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getUserOrders(userId)
                if (response.isSuccessful) {
                    response.body()?.let { orders ->
                        orders.forEach { order ->
                            localDao.insertOrder(order)
                        }
                    }
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to sync orders: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}