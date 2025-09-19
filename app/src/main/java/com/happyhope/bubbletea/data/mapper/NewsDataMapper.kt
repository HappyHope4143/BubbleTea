package com.happyhope.bubbletea.data.mapper

import com.happyhope.bubbletea.data.api.model.NewsApiModel
import com.happyhope.bubbletea.data.entity.NewsEntity
import com.happyhope.bubbletea.domain.model.News
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsDataMapper @Inject constructor() {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    
    fun mapApiToEntity(apiModel: NewsApiModel): NewsEntity? {
        if (apiModel.title.isNullOrBlank() || apiModel.url.isNullOrBlank()) {
            return null
        }
        
        val timestamp = try {
            apiModel.publishedAt?.let { dateFormat.parse(it)?.time } ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
        
        return NewsEntity(
            title = apiModel.title,
            summary = apiModel.description ?: "",
            url = apiModel.url,
            createdAt = timestamp
        )
    }
    
    fun mapEntityToDomain(entity: NewsEntity): News {
        return News(
            id = entity.id,
            title = entity.title,
            summary = entity.summary,
            url = entity.url,
            createdAt = entity.createdAt
        )
    }
    
    fun mapEntitiesToDomain(entities: List<NewsEntity>): List<News> {
        return entities.map { mapEntityToDomain(it) }
    }
    
    fun mapApiListToEntityList(apiModels: List<NewsApiModel>): List<NewsEntity> {
        return apiModels.mapNotNull { mapApiToEntity(it) }
    }
}