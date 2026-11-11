package com.example.subtrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subtrack.data.notifications.ReminderScheduler
import com.example.subtrack.ui.components.UiStateContent
import com.example.subtrack.ui.screens.HomeScreen
import com.example.subtrack.ui.screens.HomeViewModel
import com.example.subtrack.ui.screens.SettingsViewModel
import com.example.subtrack.ui.theme.SubTrackTheme

class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            ReminderScheduler.checkNow(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ReminderScheduler.schedule(this)
        requestNotificationPermission()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settings by settingsViewModel.settings.collectAsState()
            settings?.let { appSettings ->
                SubTrackTheme(settings = appSettings) {
                    val viewModel: HomeViewModel = viewModel()
                    val state by viewModel.state.collectAsState()
                    UiStateContent(state = state, onRetry = viewModel::load) { data ->
                        HomeScreen(
                            subscriptions = data.subscriptions,
                            categories = data.categories,
                            onDeleteSubscription = viewModel::deleteSubscription
                        )
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            ReminderScheduler.checkNow(this)
        }
    }
}
