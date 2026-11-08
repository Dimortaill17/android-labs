package com.example.subtrack.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.domain.AppSettings

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
    outline = TextHint,
    outlineVariant = CardBorder,
    error = Danger
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondaryContainer = PrimaryContainerDark,
    onSecondaryContainer = OnPrimaryContainerDark,
    background = BgPageDark,
    onBackground = TextLight,
    surface = SurfaceDark,
    onSurface = TextLight,
    onSurfaceVariant = TextGrayDark,
    surfaceContainerLowest = SurfaceDark,
    surfaceContainerLow = SurfaceDark,
    surfaceContainer = SurfaceDark,
    surfaceContainerHigh = SurfaceDark,
    surfaceContainerHighest = SurfaceDark,
    outline = TextHintDark,
    outlineVariant = CardBorderDark,
    error = DangerDark
)

@Composable
fun SubTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SubTrackTheme(
    settings: AppSettings,
    content: @Composable () -> Unit
) {
    val darkTheme = when (settings.theme) {
        SettingsRepository.THEME_DARK -> true
        SettingsRepository.THEME_LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    SubTrackTheme(
        darkTheme = darkTheme,
        content = content
    )
}
