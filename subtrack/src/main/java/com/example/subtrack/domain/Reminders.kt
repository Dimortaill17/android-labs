package com.example.subtrack.domain

import androidx.annotation.StringRes
import com.example.subtrack.R

object Reminders {
    val days: List<Int> = listOf(1, 3, 5)
    val labels: List<Int> = listOf(
        R.string.reminder_1_day,
        R.string.reminder_3_days,
        R.string.reminder_5_days
    )

    @StringRes
    fun label(value: Int): Int {
        val index = days.indexOf(value).coerceAtLeast(0)
        return labels[index]
    }
}
