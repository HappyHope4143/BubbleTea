package com.happyhope.bubbletea.domain.usecase

import com.happyhope.bubbletea.domain.model.News
import com.happyhope.bubbletea.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface GetNewsUseCase {
    suspend operator fun invoke(): Flow<List<News>>
}

@Singleton
class GetNewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository
) : GetNewsUseCase {
    
    override suspend fun invoke(): Flow<List<News>> {
        return newsRepository.getNews()
    }
}