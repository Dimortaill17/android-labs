package com.example.subtrack.data.utils

import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset

fun LocalDate.toEpochMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun localDateFromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
}

fun upcomingDay(today: LocalDate, day: Int): LocalDate {
    var month = YearMonth.from(today)
    while (true) {
        val date = month.atDay(minOf(day, month.lengthOfMonth()))
        if (!date.isBefore(today)) return date
        month = month.plusMonths(1)
    }
}

fun upcomingDate(today: LocalDate, month: Int, day: Int): LocalDate {
    var year = today.year
    while (true) {
        val yearMonth = YearMonth.of(year, month)
        val date = yearMonth.atDay(minOf(day, yearMonth.lengthOfMonth()))
        if (!date.isBefore(today)) return date
        year++
    }
}
