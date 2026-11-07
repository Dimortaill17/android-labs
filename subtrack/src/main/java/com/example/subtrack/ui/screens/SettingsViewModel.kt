package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.domain.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

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
}
