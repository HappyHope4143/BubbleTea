package com.happyhope.bubbletea.presentation.news

import android.content.Context
import com.happyhope.bubbletea.domain.model.News

data class NewsUiState(
    val isLoading: Boolean = false,
    val newsList: List<News> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed class NewsEvent {
    object LoadNews : NewsEvent()
    object RefreshNews : NewsEvent()
    data class RetryLoad(val message: String) : NewsEvent()
    data class NewsClicked(val news: News, val activityContext: Context? = null) : NewsEvent()
}