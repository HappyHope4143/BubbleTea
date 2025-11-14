package com.happyhope.bubbletea.domain.usecase

import com.happyhope.bubbletea.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

interface LoadMoreNewsUseCase {
    suspend operator fun invoke(page: Int): Result<Unit>
}

@Singleton
class LoadMoreNewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository
) : LoadMoreNewsUseCase {
    
    override suspend fun invoke(page: Int): Result<Unit> {
        return newsRepository.loadMoreNews(page)
    }
}
