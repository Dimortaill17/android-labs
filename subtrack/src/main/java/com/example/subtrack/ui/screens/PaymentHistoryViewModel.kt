package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.subtrack.PaymentHistoryActivity
import com.example.subtrack.data.repository.PaymentRepository
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.domain.Payment
import com.example.subtrack.domain.PaymentHistory
import com.example.subtrack.domain.PaymentHistoryData
import com.example.subtrack.domain.PaymentStatus
import com.example.subtrack.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PaymentHistoryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repository = SubscriptionRepository(application)
    private val paymentRepository = PaymentRepository(application)
    private val subscriptionId: Long = savedStateHandle[PaymentHistoryActivity.EXTRA_SUBSCRIPTION_ID] ?: -1L

    private val _state = MutableStateFlow<UiState<PaymentHistoryData>>(UiState.Loading)
    val state: StateFlow<UiState<PaymentHistoryData>> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = UiState.Loading
            combine(repository.getSubscriptions(), paymentRepository.getMarks(subscriptionId)) { subscriptions, marks ->
                val subscription = subscriptions.first { it.id == subscriptionId }
                PaymentHistoryData(subscription, PaymentHistory.build(subscription, marks))
            }
                .catch { _state.value = UiState.Error(it.message.orEmpty()) }
                .collect { _state.value = UiState.Success(it) }
        }
    }

    fun setStatus(payment: Payment, status: PaymentStatus?) {
        viewModelScope.launch {
            paymentRepository.setStatus(payment.subscriptionId, payment.date, status)
        }
    }
}
