package com.example.subtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.ui.screens.HomeScreen
import com.example.subtrack.ui.theme.SubTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = SubscriptionRepository(this)
        setContent {
            SubTrackTheme {
                HomeScreen(repository = repository)
            }
        }
    }
}
