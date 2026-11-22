package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.subtrack.data.repository.CategoryRepository
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.domain.SubscriptionsData
import com.example.subtrack.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SubscriptionRepository(application)
    private val categoryRepository = CategoryRepository(application)

    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<UiState<SubscriptionsData>> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    private fun initialState(): UiState<SubscriptionsData> {
        val subscriptions = SubscriptionRepository.cachedSubscriptions
        val categories = CategoryRepository.cachedCategories
        return if (subscriptions != null && categories != null) {
            UiState.Success(SubscriptionsData(subscriptions, categories))
        } else {
            UiState.Loading
        }
    }

    fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (_state.value is UiState.Error) {
                _state.value = UiState.Loading
            }
            combine(repository.getSubscriptions(), categoryRepository.getCategories(), ::SubscriptionsData)
                .catch { _state.value = UiState.Error(it.message.orEmpty()) }
                .collect { _state.value = UiState.Success(it) }
        }
    }
}
