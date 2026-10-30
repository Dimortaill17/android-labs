package com.example.subtrack

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.ui.screens.SettingsScreen
import com.example.subtrack.ui.theme.SubTrackTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = SettingsRepository(this)
        setContent {
            SubTrackTheme {
                SettingsScreen(repository = repository)
            }
        }
    }
}
