package com.example.subtrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Primary,
    secondaryContainer = PrimaryContainer,
    onSecondaryContainer = Primary,
    background = BgPage,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    onSurfaceVariant = TextGray,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
    outlineVariant = CardBorder,
    error = Danger
)

@Composable
fun SubTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
