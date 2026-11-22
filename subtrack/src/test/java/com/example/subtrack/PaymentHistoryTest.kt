package com.example.subtrack

import com.example.subtrack.domain.PaymentHistory
import com.example.subtrack.domain.PaymentStatus
import com.example.subtrack.domain.Subscription
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class PaymentHistoryTest {
    private val today = LocalDate.of(2026, 11, 6)

    private val subscription = Subscription(
        id = 1,
        name = "Test",
        categoryId = 11,
        price = 100.0,
        yearly = false,
        startDate = LocalDate.of(2026, 8, 10),
        endDate = null,
        remind = true,
        remindDays = 3,
        note = "",
        color = 0
    )

    @Test
    fun containsPastPaymentsAndNextOneNewestFirst() {
        val history = PaymentHistory.build(subscription, emptyMap(), today)
        assertEquals(
            listOf(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 8, 10)
            ),
            history.map { it.date }
        )
    }

    @Test
    fun unmarkedPastPaymentsAreMissedAndFutureIsUpcoming() {
        val history = PaymentHistory.build(subscription, emptyMap(), today)
        assertEquals(PaymentStatus.UPCOMING, history[0].status)
        assertEquals(PaymentStatus.MISSED, history[1].status)
    }

    @Test
    fun usesSavedMarks() {
        val marks = mapOf(
            LocalDate.of(2026, 10, 10) to PaymentStatus.PAID,
            LocalDate.of(2026, 9, 10) to PaymentStatus.FAILED
        )
        val history = PaymentHistory.build(subscription, marks, today)
        assertEquals(PaymentStatus.PAID, history[1].status)
        assertEquals(PaymentStatus.FAILED, history[2].status)
        assertEquals(PaymentStatus.MISSED, history[3].status)
    }

    @Test
    fun finishedSubscriptionHasOnlyItsPayments() {
        val finished = subscription.copy(endDate = LocalDate.of(2026, 9, 30))
        val history = PaymentHistory.build(finished, emptyMap(), today)
        assertEquals(2, history.size)
    }
}
