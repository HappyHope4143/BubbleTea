package com.happyhope.bubbletea.data.api.service

import com.happyhope.bubbletea.data.api.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String = "bubble tea",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String
    ): NewsResponse
    
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}