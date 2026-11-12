package com.example.subtrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.subtrack.domain.AppSettings
import com.example.subtrack.domain.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { preferences ->
        AppSettings(
            theme = preferences[KEY_THEME] ?: THEME_LIGHT,
            language = preferences[KEY_LANGUAGE] ?: LANGUAGE_RU,
            sortOrder = SortOrder.entries.find { it.name == preferences[KEY_SORT_ORDER] } ?: SortOrder.PRICE_DESC
        )
    }.onEach { cachedSettings = it }

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

    suspend fun setSortOrder(sortOrder: SortOrder) {
        context.settingsDataStore.edit { preferences ->
            preferences[KEY_SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun clear() {
        context.settingsDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        var cachedSettings: AppSettings? = null
            private set

        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_SORT_ORDER = stringPreferencesKey("sort_order")

        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
        const val LANGUAGE_RU = "ru"
        const val LANGUAGE_EN = "en"
    }
}
