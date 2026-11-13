package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.subtrack.data.notifications.PaymentNotifications
import com.example.subtrack.data.repository.CategoryRepository
import com.example.subtrack.data.repository.ReminderRepository
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.domain.AppSettings
import com.example.subtrack.domain.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)
    private val subscriptionRepository = SubscriptionRepository(application)
    private val categoryRepository = CategoryRepository(application)
    private val reminderRepository = ReminderRepository(application)

    private val _settings = MutableStateFlow(SettingsRepository.cachedSettings)
    val settings: StateFlow<AppSettings?> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settings.collect { _settings.value = it }
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }

    fun setSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            repository.setSortOrder(sortOrder)
        }
    }

    fun setHideAmounts(hideAmounts: Boolean) {
        viewModelScope.launch {
            repository.setHideAmounts(hideAmounts)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            subscriptionRepository.deleteAllSubscriptions()
            categoryRepository.resetCategories()
            reminderRepository.clear()
            repository.clear()
            PaymentNotifications.cancelAll(getApplication<Application>())
        }
    }
}
