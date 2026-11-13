package com.example.subtrack.domain

import androidx.annotation.StringRes
import com.example.subtrack.R
import java.time.LocalDate

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

    fun key(subscriptionId: Long, paymentDate: LocalDate): String {
        return "${subscriptionId}_$paymentDate"
    }

    fun actualKeys(keys: Set<String>, today: LocalDate): Set<String> {
        return keys.filter { key ->
            val date = runCatching { LocalDate.parse(key.substringAfter('_')) }.getOrNull()
            date != null && !date.isBefore(today)
        }.toSet()
    }
}
