package com.example.subtrack.data.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun LocalDate.toEpochMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun localDateFromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
}
