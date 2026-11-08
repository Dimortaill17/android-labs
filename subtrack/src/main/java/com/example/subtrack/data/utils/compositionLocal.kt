package com.example.subtrack.data.utils

import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

val LocalAppLocale = staticCompositionLocalOf { Locale.getDefault() }
