package com.example.subtrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    val theme: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[KEY_THEME] ?: THEME_LIGHT
    }

    val language: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: LANGUAGE_RU
    }

    suspend fun setTheme(theme: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[KEY_THEME] = theme
        }
    }

    suspend fun setLanguage(language: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language
        }
    }

    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_LANGUAGE = stringPreferencesKey("language")

        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
        const val LANGUAGE_RU = "ru"
        const val LANGUAGE_EN = "en"
    }
}
