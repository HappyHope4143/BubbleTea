package com.happyhope.bubbletea.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 사용자 주문 데이터 모델
 */
@Serializable
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val id: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val totalPrice: Double,
    val orderTime: Long,
    val status: OrderStatus = OrderStatus.PENDING,
    val customizations: List<String> = emptyList()
)

@Serializable
enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}