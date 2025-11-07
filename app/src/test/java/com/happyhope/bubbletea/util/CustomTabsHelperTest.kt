package com.happyhope.bubbletea.util

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomTabsHelperTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun `openUrl handles valid URL gracefully`() {
        // Given
        val url = "https://example.com"
        
        // When - should not throw exception
        CustomTabsHelper.openUrl(mockContext, url)
        
        // Then - verify it doesn't crash
        // Note: Actual UI behavior would be tested in instrumented tests
        // This test ensures the method handles the call gracefully
    }

    @Test
    fun `openUrl handles invalid URL gracefully`() {
        // Given
        val invalidUrl = "not-a-valid-url"
        
        // When - should not throw exception
        CustomTabsHelper.openUrl(mockContext, invalidUrl)
        
        // Then - should handle gracefully without crash
        // Error is printed but doesn't propagate
    }

    @Test
    fun `openUrl handles empty URL gracefully`() {
        // Given
        val emptyUrl = ""
        
        // When - should not throw exception
        CustomTabsHelper.openUrl(mockContext, emptyUrl)
        
        // Then - should handle gracefully without crash
    }

    @Test
    fun `openUrl with toolbar color handles valid URL gracefully`() {
        // Given
        val url = "https://example.com"
        val toolbarColor = 0xFF6200EE.toInt()
        
        // When - should not throw exception
        CustomTabsHelper.openUrl(mockContext, url, toolbarColor)
        
        // Then - verify it doesn't crash with color parameter
    }
}
