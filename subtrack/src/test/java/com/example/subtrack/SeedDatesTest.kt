package com.example.subtrack

import com.example.subtrack.data.MockData
import com.example.subtrack.data.utils.upcomingDate
import com.example.subtrack.data.utils.upcomingDay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class SeedDatesTest {
    private val today = LocalDate.of(2026, 10, 23)

    @Test
    fun upcomingDay_isNeverInThePast() {
        assertEquals(LocalDate.of(2026, 10, 28), upcomingDay(today, 28))
        assertEquals(LocalDate.of(2026, 11, 10), upcomingDay(today, 10))
        assertEquals(LocalDate.of(2026, 10, 23), upcomingDay(today, 23))
    }

    @Test
    fun upcomingDate_isNeverInThePast() {
        assertEquals(LocalDate.of(2026, 12, 14), upcomingDate(today, 12, 14))
        assertEquals(LocalDate.of(2027, 3, 5), upcomingDate(today, 3, 5))
    }

    @Test
    fun mockSubscriptions_haveNoPaymentsInPastMonths() {
        val previousMonth = YearMonth.from(today).minusMonths(1)
        MockData.subscriptions(today).forEach { subscription ->
            assertNull(subscription.paymentIn(previousMonth))
        }
    }
}
