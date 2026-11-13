package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.subtrack.SubscriptionFormActivity
import com.example.subtrack.data.notifications.ReminderScheduler
import com.example.subtrack.data.repository.CategoryRepository
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.domain.Subscription
import com.example.subtrack.domain.SubscriptionFormData
import com.example.subtrack.ui.UiState
import com.example.subtrack.ui.loadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SubscriptionFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repository = SubscriptionRepository(application)
    private val categoryRepository = CategoryRepository(application)
    private val subscriptionId: Long = savedStateHandle[SubscriptionFormActivity.EXTRA_SUBSCRIPTION_ID] ?: -1L

    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<UiState<SubscriptionFormData>> = _state.asStateFlow()

    init {
        load()
    }

    private fun initialState(): UiState<SubscriptionFormData> {
        val categories = CategoryRepository.cachedCategories ?: return UiState.Loading
        if (subscriptionId == -1L) return UiState.Success(SubscriptionFormData(null, categories))
        val cached = SubscriptionRepository.cachedSubscriptions?.find { it.id == subscriptionId }
        return if (cached != null) UiState.Success(SubscriptionFormData(cached, categories)) else UiState.Loading
    }

    fun load() {
        if (_state.value is UiState.Success) return
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = loadUiState {
                SubscriptionFormData(
                    subscription = repository.getSubscription(subscriptionId),
                    categories = categoryRepository.getCategories().first()
                )
            }
        }
    }

    fun saveSubscription(subscription: Subscription, onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.saveSubscription(subscription)
            ReminderScheduler.checkNow(getApplication<Application>())
            onSaved()
        }
    }
}
