package com.happyhope.bubbletea.data.repository

import com.happyhope.bubbletea.data.api.service.NewsApiService
import com.happyhope.bubbletea.data.dao.NewsDao
import com.happyhope.bubbletea.data.entity.NewsEntity
import com.happyhope.bubbletea.data.local.SampleNewsProvider
import com.happyhope.bubbletea.data.mapper.NewsDataMapper
import com.happyhope.bubbletea.domain.model.News
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NewsRepositoryImplTest {

    @Mock
    private lateinit var newsApiService: NewsApiService
    
    @Mock
    private lateinit var newsDao: NewsDao
    
    @Mock
    private lateinit var mapper: NewsDataMapper
    
    @Mock
    private lateinit var sampleNewsProvider: SampleNewsProvider
    
    private lateinit var repository: NewsRepositoryImpl
    
    companion object {
        private const val TEST_TITLE = "Test News"
        private const val TEST_SUMMARY = "Test Summary"
        private const val TEST_URL = "https://test.com"
        private const val TEST_SOURCE = "Test Source"
    }
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = NewsRepositoryImpl(newsApiService, newsDao, mapper, sampleNewsProvider)
    }
    
    @Test
    fun `getLocalNews returns mapped news from DAO`() = runTest {
        // Given
        val entities = listOf(
            NewsEntity(
                id = 1,
                title = TEST_TITLE,
                summary = TEST_SUMMARY,
                url = TEST_URL,
                source = TEST_SOURCE,
                createdAt = System.currentTimeMillis()
            )
        )
        val expectedNews = listOf(
            News(
                id = 1,
                title = TEST_TITLE,
                summary = TEST_SUMMARY,
                url = TEST_URL,
                source = TEST_SOURCE,
                createdAt = System.currentTimeMillis()
            )
        )
        
        `when`(newsDao.getNewsWithLimit(100)).thenReturn(entities)
        `when`(mapper.mapEntitiesToDomain(entities)).thenReturn(expectedNews)
        
        // When
        val result = repository.getLocalNews()
        
        // Then
        assertEquals(expectedNews, result)
        verify(newsDao).getNewsWithLimit(100)
        verify(mapper).mapEntitiesToDomain(entities)
    }
    
    @Test
    fun `clearOldNews deletes excess news when over limit`() = runTest {
        // Given
        val currentCount = 150
        val limit = 100
        val excessCount = 50
        
        `when`(newsDao.getNewsCount()).thenReturn(currentCount)
        
        // When
        repository.clearOldNews()
        
        // Then
        verify(newsDao).getNewsCount()
        verify(newsDao).deleteOldestNews(excessCount)
    }
    
    @Test
    fun `clearOldNews does nothing when under limit`() = runTest {
        // Given
        val currentCount = 50
        
        `when`(newsDao.getNewsCount()).thenReturn(currentCount)
        
        // When
        repository.clearOldNews()
        
        // Then
        verify(newsDao).getNewsCount()
        verify(newsDao, never()).deleteOldestNews(anyInt())
    }
}