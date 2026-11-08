package com.example.subtrack.data.utils

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

fun Context.withLocale(locale: Locale): Context {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration)
}

@Composable
fun localizedStringResource(@StringRes resId: Int, vararg args: Any): String {
    val context = LocalContext.current
    val locale = LocalAppLocale.current
    val localizedContext = remember(context, locale) { context.withLocale(locale) }
    return localizedContext.resources.getString(resId, *args)
}
