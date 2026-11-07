package com.example.subtrack.ui.components

import android.app.Activity
import androidx.annotation.DrawableRes
import com.example.subtrack.MainActivity
import com.example.subtrack.R
import com.example.subtrack.SettingsActivity
import com.example.subtrack.StatisticsActivity

sealed class Screen(
    val title: String,
    @DrawableRes val icon: Int,
    val activity: Class<out Activity>
) {
    data object Home : Screen(
        title = "Главная",
        icon = R.drawable.ic_home,
        activity = MainActivity::class.java
    )

    data object Statistics : Screen(
        title = "Статистика",
        icon = R.drawable.ic_chart,
        activity = StatisticsActivity::class.java
    )

    data object Settings : Screen(
        title = "Настройки",
        icon = R.drawable.ic_settings,
        activity = SettingsActivity::class.java
    )
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Statistics,
    Screen.Settings
)
