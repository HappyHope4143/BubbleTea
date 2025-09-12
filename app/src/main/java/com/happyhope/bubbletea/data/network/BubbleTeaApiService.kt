package com.happyhope.bubbletea.data.network

import com.happyhope.bubbletea.data.model.Order
import com.happyhope.bubbletea.data.model.TeaProduct
import retrofit2.Response
import retrofit2.http.*

/**
 * 버블티 API 서비스 인터페이스
 */
interface BubbleTeaApiService {
    
    /**
     * 모든 티 제품 조회
     */
    @GET("products")
    suspend fun getAllProducts(): Response<List<TeaProduct>>
    
    /**
     * 카테고리별 티 제품 조회
     */
    @GET("products")
    suspend fun getProductsByCategory(@Query("category") category: String): Response<List<TeaProduct>>
    
    /**
     * 특정 제품 상세 조회
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: String): Response<TeaProduct>
    
    /**
     * 주문 생성
     */
    @POST("orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
    
    /**
     * 사용자 주문 내역 조회
     */
    @GET("orders")
    suspend fun getUserOrders(@Query("userId") userId: String): Response<List<Order>>
    
    /**
     * 주문 상태 업데이트
     */
    @PUT("orders/{id}")
    suspend fun updateOrderStatus(
        @Path("id") orderId: String,
        @Body order: Order
    ): Response<Order>
    
    /**
     * 주문 취소
     */
    @DELETE("orders/{id}")
    suspend fun cancelOrder(@Path("id") orderId: String): Response<Unit>
}