package com.example.subtrack.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(currentScreen: Screen) {
    val activity = LocalContext.current as Activity

    NavigationBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 32.dp)
    ) {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                onClick = { openScreen(activity, currentScreen, screen) },
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = screen.title
                    )
                },
                label = { Text(text = screen.title) }
            )
        }
    }
}

private fun openScreen(activity: Activity, current: Screen, target: Screen) {
    if (current == target) return

    val intent = Intent(activity, target.activity)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    activity.startActivity(intent)

    @Suppress("DEPRECATION")
    activity.overridePendingTransition(0, 0)
}
