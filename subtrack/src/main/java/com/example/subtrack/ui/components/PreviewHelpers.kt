package com.example.subtrack.ui.components

import androidx.compose.runtime.Composable
import com.example.subtrack.data.MockData
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.domain.AppSettings
import com.example.subtrack.domain.Categories
import com.example.subtrack.domain.Category
import com.example.subtrack.domain.SortOrder
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.theme.SubTrackTheme

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    SubTrackTheme {
        content()
    }
}

val sampleSubscriptions: List<Subscription> = MockData.subscriptions()
val sampleSubscription: Subscription = sampleSubscriptions.first()
val sampleYearlySubscription: Subscription = sampleSubscriptions.last()
val sampleCategories: List<Category> = Categories.defaults
val sampleSettings = AppSettings(
    theme = SettingsRepository.THEME_LIGHT,
    language = SettingsRepository.LANGUAGE_RU,
    sortOrder = SortOrder.PRICE_DESC,
    hideAmounts = false
)
