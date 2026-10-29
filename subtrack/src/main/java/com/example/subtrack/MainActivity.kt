package com.example.subtrack

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.ui.screens.HomeScreen
import com.example.subtrack.ui.theme.SubTrackTheme

class MainActivity : ComponentActivity() {
    private var refreshKey by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = SubscriptionRepository(this)
        setContent {
            SubTrackTheme {
                HomeScreen(
                    repository = repository,
                    refreshKey = refreshKey
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshKey++
    }
}
