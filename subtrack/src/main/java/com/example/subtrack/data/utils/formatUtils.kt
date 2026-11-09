package com.example.subtrack.data.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object Formats {
    private val russian = Locale.forLanguageTag("ru-RU")
    private val moneyFormat = NumberFormat.getNumberInstance(russian).apply { maximumFractionDigits = 2 }
    private val inputDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun money(value: Double): String {
        return moneyFormat.format(value) + " ₽"
    }

    fun plain(value: Double): String {
        return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
    }

    fun longDate(date: LocalDate, locale: Locale): String {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale)
        return date.format(formatter)
    }

    fun inputDate(date: LocalDate): String {
        return date.format(inputDateFormat)
    }

    fun monthTitle(month: YearMonth, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", locale)
        return month.format(formatter).replaceFirstChar { it.titlecase(locale) }
    }
}
