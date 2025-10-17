package com.happyhope.bubbletea.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happyhope.bubbletea.domain.model.News
import com.happyhope.bubbletea.domain.usecase.GetNewsUseCase
import com.happyhope.bubbletea.domain.usecase.RefreshNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    
    init {
        handleEvent(NewsEvent.LoadNews)
    }
    
    fun handleEvent(event: NewsEvent) {
        when (event) {
            is NewsEvent.LoadNews -> loadNews()
            is NewsEvent.RefreshNews -> refreshNews()
            is NewsEvent.RetryLoad -> loadNews()
            is NewsEvent.NewsClicked -> handleNewsClick(event.news)
        }
    }
    
    private fun loadNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                getNewsUseCase()
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                    .collect { newsList ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            newsList = newsList,
                            error = null
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load news"
                )
            }
        }
    }
    
    private fun refreshNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            val result = refreshNewsUseCase()
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isRefreshing = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = "Failed to refresh: ${exception.message}"
                    )
                }
            )
        }
    }
    
    private fun handleNewsClick(news: News) {
        // TODO: Handle opening news URL in browser or WebView
        // For now, this is a placeholder for future implementation
    }
}