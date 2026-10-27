package com.example.subtrack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.subtrack.R

val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold)
)

private val DefaultTypography = Typography()

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = Manrope),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = Manrope),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = Manrope),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = Manrope),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = Manrope),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = Manrope),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = Manrope),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = Manrope),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = Manrope),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = Manrope),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = Manrope),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = Manrope),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = Manrope),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = Manrope),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = Manrope)
)
