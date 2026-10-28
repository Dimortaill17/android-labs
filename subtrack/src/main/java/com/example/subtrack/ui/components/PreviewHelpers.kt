package com.example.subtrack.ui.components

import androidx.compose.runtime.Composable
import com.example.subtrack.data.MockData
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.theme.SubTrackTheme

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    SubTrackTheme {
        content()
    }
}

val sampleSubscription: Subscription = MockData.subscriptions().first()
val sampleYearlySubscription: Subscription = MockData.subscriptions().last()
