package com.example.subtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.subtrack.ui.components.UiStateContent
import com.example.subtrack.ui.screens.CategoriesScreen
import com.example.subtrack.ui.screens.CategoriesViewModel
import com.example.subtrack.ui.screens.SettingsViewModel
import com.example.subtrack.ui.theme.SubTrackTheme

class CategoriesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settings by settingsViewModel.settings.collectAsState()
            settings?.let { appSettings ->
                SubTrackTheme(settings = appSettings) {
                    val viewModel: CategoriesViewModel = viewModel()
                    val state by viewModel.state.collectAsState()
                    UiStateContent(state = state, onRetry = viewModel::load) { categories ->
                        CategoriesScreen(
                            categories = categories,
                            onBack = { finish() },
                            onSaveCategory = viewModel::saveCategory,
                            onDeleteCategory = viewModel::deleteCategory
                        )
                    }
                }
            }
        }
    }
}
