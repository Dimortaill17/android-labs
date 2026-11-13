package com.example.subtrack.data.utils

import androidx.compose.runtime.Composable

@Composable
fun moneyText(value: Double): String {
    return if (LocalHideAmounts.current) Formats.HIDDEN_AMOUNT else Formats.money(value)
}
