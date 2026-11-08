package com.example.subtrack.ui.components

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.subtrack.MainActivity
import com.example.subtrack.R
import com.example.subtrack.SettingsActivity
import com.example.subtrack.StatisticsActivity

sealed class Screen(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val activity: Class<out Activity>
) {
    data object Home : Screen(
        title = R.string.nav_home,
        icon = R.drawable.ic_home,
        activity = MainActivity::class.java
    )

    data object Statistics : Screen(
        title = R.string.nav_statistics,
        icon = R.drawable.ic_chart,
        activity = StatisticsActivity::class.java
    )

    data object Settings : Screen(
        title = R.string.nav_settings,
        icon = R.drawable.ic_settings,
        activity = SettingsActivity::class.java
    )
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Statistics,
    Screen.Settings
)
