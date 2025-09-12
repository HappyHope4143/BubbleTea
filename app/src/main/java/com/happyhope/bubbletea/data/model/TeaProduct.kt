package com.happyhope.bubbletea.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 버블티 제품 데이터 모델
 */
@Serializable
@Entity(tableName = "tea_products")
data class TeaProduct(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val isAvailable: Boolean = true,
    val ingredients: List<String> = emptyList()
)