package com.example.subtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subtrack.ui.components.UiStateContent
import com.example.subtrack.ui.screens.HomeScreen
import com.example.subtrack.ui.screens.HomeViewModel
import com.example.subtrack.ui.theme.SubTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SubTrackTheme {
                val viewModel: HomeViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                UiStateContent(state = state, onRetry = viewModel::load) { subscriptions ->
                    HomeScreen(
                        subscriptions = subscriptions,
                        onDeleteSubscription = viewModel::deleteSubscription
                    )
                }
            }
        }
    }
}
