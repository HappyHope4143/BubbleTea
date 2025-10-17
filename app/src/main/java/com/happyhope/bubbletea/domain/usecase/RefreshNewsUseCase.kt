package com.happyhope.bubbletea.domain.usecase

import com.happyhope.bubbletea.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

interface RefreshNewsUseCase {
    suspend operator fun invoke(): Result<Unit>
}

@Singleton
class RefreshNewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository
) : RefreshNewsUseCase {
    
    override suspend fun invoke(): Result<Unit> {
        return newsRepository.refreshNews()
    }
}