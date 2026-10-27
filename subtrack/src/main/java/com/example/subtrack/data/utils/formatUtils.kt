package com.example.subtrack.data.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

object Formats {
    private val russian = Locale.forLanguageTag("ru-RU")
    private val moneyFormat = NumberFormat.getNumberInstance(russian).apply { maximumFractionDigits = 2 }
    private val longDateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy 'г.'", russian)
    private val inputDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val monthFormat = DateTimeFormatter.ofPattern("LLLL yyyy 'Г.'", russian)

    fun money(value: Double): String {
        return moneyFormat.format(value) + " ₽"
    }

    fun plain(value: Double): String {
        return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
    }

    fun longDate(date: LocalDate): String {
        return date.format(longDateFormat)
    }

    fun inputDate(date: LocalDate): String {
        return date.format(inputDateFormat)
    }

    fun monthTitle(month: YearMonth): String {
        return month.format(monthFormat).replaceFirstChar { it.titlecase(russian) }
    }
}
