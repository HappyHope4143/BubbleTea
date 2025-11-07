package com.happyhope.bubbletea.data.dao

import com.happyhope.bubbletea.data.entity.NewsEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NewsDaoTest {

    @Mock
    private lateinit var newsDao: NewsDao
    
    private val testNewsEntity1 = NewsEntity(
        id = 1,
        title = "Test News 1",
        summary = "Summary 1",
        url = "https://test1.com",
        source = "Source 1",
        createdAt = System.currentTimeMillis()
    )
    
    private val testNewsEntity2 = NewsEntity(
        id = 2,
        title = "Test News 2",
        summary = "Summary 2",
        url = "https://test2.com",
        source = "Source 2",
        createdAt = System.currentTimeMillis()
    )
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }
    
    @Test
    fun `getAllUrls returns list of URLs from database`() = runTest {
        // Given
        val expectedUrls = listOf("https://test1.com", "https://test2.com")
        `when`(newsDao.getAllUrls()).thenReturn(expectedUrls)
        
        // When
        val result = newsDao.getAllUrls()
        
        // Then
        assertEquals(expectedUrls, result)
        verify(newsDao).getAllUrls()
    }
    
    @Test
    fun `getAllUrls returns empty list when no news exists`() = runTest {
        // Given
        `when`(newsDao.getAllUrls()).thenReturn(emptyList())
        
        // When
        val result = newsDao.getAllUrls()
        
        // Then
        assertTrue(result.isEmpty())
        verify(newsDao).getAllUrls()
    }
    
    @Test
    fun `getNewsByUrl returns news entity when URL exists`() = runTest {
        // Given
        val url = "https://test1.com"
        `when`(newsDao.getNewsByUrl(url)).thenReturn(testNewsEntity1)
        
        // When
        val result = newsDao.getNewsByUrl(url)
        
        // Then
        assertNotNull(result)
        assertEquals(testNewsEntity1, result)
        verify(newsDao).getNewsByUrl(url)
    }
    
    @Test
    fun `getNewsByUrl returns null when URL does not exist`() = runTest {
        // Given
        val url = "https://nonexistent.com"
        `when`(newsDao.getNewsByUrl(url)).thenReturn(null)
        
        // When
        val result = newsDao.getNewsByUrl(url)
        
        // Then
        assertNull(result)
        verify(newsDao).getNewsByUrl(url)
    }
    
    @Test
    fun `getNewsCount returns correct count`() = runTest {
        // Given
        val expectedCount = 5
        `when`(newsDao.getNewsCount()).thenReturn(expectedCount)
        
        // When
        val result = newsDao.getNewsCount()
        
        // Then
        assertEquals(expectedCount, result)
        verify(newsDao).getNewsCount()
    }
}
