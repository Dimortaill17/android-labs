package com.example.subtrack

import com.example.subtrack.domain.Subscription
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PaymentReminderTest {
    private fun subscription(
        startDate: LocalDate,
        remind: Boolean = true,
        remindDays: Int = 3,
        yearly: Boolean = false,
        endDate: LocalDate? = null
    ) = Subscription(
        id = 1,
        name = "Test",
        categoryId = 11,
        price = 100.0,
        yearly = yearly,
        startDate = startDate,
        endDate = endDate,
        remind = remind,
        remindDays = remindDays,
        note = "",
        color = 0
    )

    private fun date(year: Int, month: Int, day: Int): LocalDate = LocalDate.of(year, month, day)

    @Test
    fun remindsExactlyBeforePayment() {
        val subscription = subscription(date(2026, 1, 15), remindDays = 3)
        assertTrue(subscription.shouldRemind(date(2026, 11, 12)))
        assertFalse(subscription.shouldRemind(date(2026, 11, 11)))
        assertFalse(subscription.shouldRemind(date(2026, 11, 13)))
    }

    @Test
    fun respectsSelectedReminderDays() {
        val subscription = subscription(date(2026, 1, 15), remindDays = 1)
        assertTrue(subscription.shouldRemind(date(2026, 11, 14)))
        assertFalse(subscription.shouldRemind(date(2026, 11, 12)))
    }

    @Test
    fun doesNotRemindWhenReminderIsOff() {
        val subscription = subscription(date(2026, 1, 15), remind = false)
        assertFalse(subscription.shouldRemind(date(2026, 11, 12)))
    }

    @Test
    fun doesNotRemindAfterEndDate() {
        val subscription = subscription(date(2026, 1, 15), endDate = date(2026, 10, 15))
        assertFalse(subscription.shouldRemind(date(2026, 11, 12)))
    }

    @Test
    fun remindsBeforeYearlyPayment() {
        val subscription = subscription(date(2025, 12, 1), remindDays = 5, yearly = true)
        assertTrue(subscription.shouldRemind(date(2026, 11, 26)))
        assertFalse(subscription.shouldRemind(date(2026, 10, 27)))
    }
}
