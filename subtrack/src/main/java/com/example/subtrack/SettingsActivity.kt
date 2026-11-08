package com.example.subtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subtrack.ui.screens.SettingsScreen
import com.example.subtrack.ui.screens.SettingsViewModel
import com.example.subtrack.ui.theme.SubTrackTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SettingsViewModel = viewModel()
            val settings by viewModel.settings.collectAsState()
            settings?.let { appSettings ->
                SubTrackTheme(settings = appSettings) {
                    SettingsScreen(
                        settings = appSettings,
                        onThemeChange = viewModel::setTheme,
                        onLanguageChange = viewModel::setLanguage
                    )
                }
            }
        }
    }
}
