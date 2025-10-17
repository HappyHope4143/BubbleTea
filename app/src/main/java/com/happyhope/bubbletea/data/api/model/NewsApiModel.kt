package com.happyhope.bubbletea.data.api.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: List<NewsApiModel>,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("totalResults")
    val totalResults: Int
)

data class NewsApiModel(
    @SerializedName("title")
    val title: String?,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("url")
    val url: String?,
    
    @SerializedName("publishedAt")
    val publishedAt: String?,
    
    @SerializedName("urlToImage")
    val urlToImage: String?,
    
    @SerializedName("source")
    val source: NewsSourceModel?
)

data class NewsSourceModel(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("name")
    val name: String?
)