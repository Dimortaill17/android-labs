package com.example.subtrack.data.repository

import android.content.Context

class SettingsRepository(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getTheme(): String {
        return prefs.getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
    }

    fun setTheme(theme: String) {
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_RU) ?: LANGUAGE_RU
    }

    fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val KEY_THEME = "theme"
        private const val KEY_LANGUAGE = "language"

        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
        const val LANGUAGE_RU = "ru"
        const val LANGUAGE_EN = "en"
    }
}
