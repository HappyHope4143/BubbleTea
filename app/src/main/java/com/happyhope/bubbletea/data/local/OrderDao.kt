package com.happyhope.bubbletea.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.happyhope.bubbletea.data.model.Order
import com.happyhope.bubbletea.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

/**
 * 주문 데이터 접근 객체
 */
@Dao
interface OrderDao {
    
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderTime DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: String): Order?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
    
    @Update
    suspend fun updateOrder(order: Order)
    
    @Delete
    suspend fun deleteOrder(order: Order)
    
    @Query("DELETE FROM orders WHERE status = :status")
    suspend fun deleteOrdersByStatus(status: OrderStatus)
}