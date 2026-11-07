package com.example.subtrack.domain

import java.time.LocalDate
import java.time.YearMonth

data class Subscription(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val yearly: Boolean,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val remind: Boolean,
    val remindDays: Int,
    val note: String,
    val color: Int
) {
    private fun paymentDates(): Sequence<LocalDate> =
        generateSequence(0L) { it + 1 }
            .map { if (yearly) startDate.plusYears(it) else startDate.plusMonths(it) }
            .takeWhile { endDate == null || !it.isAfter(endDate) }

    fun nextPayment(from: LocalDate = LocalDate.now()): LocalDate? {
        return paymentDates().firstOrNull { !it.isBefore(from) }
    }

    fun paymentsBetween(from: LocalDate, to: LocalDate): Int {
        return paymentDates().takeWhile { !it.isAfter(to) }.count { !it.isBefore(from) }
    }

    fun paymentIn(month: YearMonth): LocalDate? {
        return paymentDates()
            .takeWhile { !YearMonth.from(it).isAfter(month) }
            .firstOrNull { YearMonth.from(it) == month }
    }
}
