package com.example.subtrack

import com.example.subtrack.domain.Subscription
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class SubscriptionScheduleTest {
    private fun subscription(
        startDate: LocalDate,
        yearly: Boolean = false,
        endDate: LocalDate? = null
    ) = Subscription(
        id = 1,
        name = "Test",
        category = "Другое",
        price = 100.0,
        yearly = yearly,
        startDate = startDate,
        endDate = endDate,
        remind = false,
        remindDays = 3,
        note = "",
        color = 0
    )

    private fun date(year: Int, month: Int, day: Int): LocalDate = LocalDate.of(year, month, day)

    @Test
    fun monthly_hasNoPaymentsBeforeStartDate() {
        val subscription = subscription(date(2026, 10, 28))
        assertNull(subscription.paymentIn(YearMonth.of(2026, 9)))
        assertNull(subscription.paymentIn(YearMonth.of(2026, 1)))
        assertEquals(date(2026, 10, 28), subscription.paymentIn(YearMonth.of(2026, 10)))
        assertEquals(date(2027, 3, 28), subscription.paymentIn(YearMonth.of(2027, 3)))
    }

    @Test
    fun monthly_countsPaymentsInYear() {
        val subscription = subscription(date(2026, 10, 28))
        assertEquals(3, subscription.paymentsBetween(date(2026, 1, 1), date(2026, 12, 31)))
        assertEquals(12, subscription.paymentsBetween(date(2027, 1, 1), date(2027, 12, 31)))
    }

    @Test
    fun monthly_lastDayOfMonthDoesNotDrift() {
        val subscription = subscription(date(2026, 1, 31))
        assertEquals(date(2026, 2, 28), subscription.paymentIn(YearMonth.of(2026, 2)))
        assertEquals(date(2026, 3, 31), subscription.paymentIn(YearMonth.of(2026, 3)))
        assertEquals(date(2026, 4, 30), subscription.paymentIn(YearMonth.of(2026, 4)))
    }

    @Test
    fun yearly_paysOncePerYearInItsMonth() {
        val subscription = subscription(date(2026, 12, 14), yearly = true)
        assertEquals(date(2026, 12, 14), subscription.paymentIn(YearMonth.of(2026, 12)))
        assertNull(subscription.paymentIn(YearMonth.of(2027, 11)))
        assertEquals(date(2027, 12, 14), subscription.paymentIn(YearMonth.of(2027, 12)))
        assertEquals(1, subscription.paymentsBetween(date(2027, 1, 1), date(2027, 12, 31)))
    }

    @Test
    fun yearly_leapDayFallsBackTo28th() {
        val subscription = subscription(date(2028, 2, 29), yearly = true)
        assertEquals(date(2029, 2, 28), subscription.paymentIn(YearMonth.of(2029, 2)))
        assertEquals(date(2032, 2, 29), subscription.paymentIn(YearMonth.of(2032, 2)))
    }

    @Test
    fun endDate_isInclusive() {
        val subscription = subscription(date(2026, 10, 15), endDate = date(2027, 1, 15))
        assertEquals(date(2027, 1, 15), subscription.paymentIn(YearMonth.of(2027, 1)))
        assertNull(subscription.paymentIn(YearMonth.of(2027, 2)))
        assertEquals(3, subscription.paymentsBetween(date(2026, 1, 1), date(2026, 12, 31)))
        assertEquals(1, subscription.paymentsBetween(date(2027, 1, 1), date(2027, 12, 31)))
    }

    @Test
    fun endDate_beforePaymentDayExcludesThatMonth() {
        val subscription = subscription(date(2026, 10, 15), endDate = date(2027, 1, 10))
        assertNull(subscription.paymentIn(YearMonth.of(2027, 1)))
        assertNull(subscription.nextPayment(date(2027, 1, 1)))
    }

    @Test
    fun nextPayment_returnsUpcomingDate() {
        assertEquals(date(2026, 11, 10), subscription(date(2026, 1, 10)).nextPayment(date(2026, 10, 23)))
        assertEquals(date(2026, 10, 23), subscription(date(2026, 1, 23)).nextPayment(date(2026, 10, 23)))
    }
}
