package com.example.subtrack.domain

object Reminders {
    val days: List<Int> = listOf(1, 3, 5)
    val labels: List<String> = listOf("За 1 день", "За 3 дня", "За 5 дней")

    fun label(value: Int): String {
        val index = days.indexOf(value).coerceAtLeast(0)
        return labels[index]
    }
}
