package com.example.subtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.ui.screens.SubscriptionFormScreen
import com.example.subtrack.ui.theme.SubTrackTheme
import kotlinx.coroutines.launch

class SubscriptionFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = SubscriptionRepository(this)
        val subscriptionId = intent.getLongExtra(EXTRA_SUBSCRIPTION_ID, -1L)
        lifecycleScope.launch {
            val subscription = repository.getSubscription(subscriptionId)
            setContent {
                SubTrackTheme {
                    SubscriptionFormScreen(
                        subscription = subscription,
                        onBack = { finish() },
                        onSave = {
                            lifecycleScope.launch {
                                repository.saveSubscription(it)
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_SUBSCRIPTION_ID = "subscription_id"
    }
}
