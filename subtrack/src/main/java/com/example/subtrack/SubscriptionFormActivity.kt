package com.example.subtrack

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.ui.screens.SubscriptionFormScreen
import com.example.subtrack.ui.theme.SubTrackTheme

class SubscriptionFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = SubscriptionRepository(this)
        val subscriptionId = intent.getLongExtra(EXTRA_SUBSCRIPTION_ID, -1L)
        val subscription = repository.getSubscriptions().find { it.id == subscriptionId }
        setContent {
            SubTrackTheme {
                SubscriptionFormScreen(
                    subscription = subscription,
                    onBack = { finish() },
                    onSave = {
                        repository.saveSubscription(it)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_SUBSCRIPTION_ID = "subscription_id"
    }
}
