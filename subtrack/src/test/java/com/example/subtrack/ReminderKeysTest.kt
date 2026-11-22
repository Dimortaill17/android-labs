package com.example.subtrack

import com.example.subtrack.domain.Reminders
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ReminderKeysTest {
    private val today = LocalDate.of(2026, 11, 6)

    @Test
    fun keyContainsSubscriptionAndPaymentDate() {
        assertEquals("5_2026-11-07", Reminders.key(5, LocalDate.of(2026, 11, 7)))
    }

    @Test
    fun keepsOnlyKeysForTodayAndLater() {
        val keys = setOf(
            Reminders.key(1, LocalDate.of(2026, 11, 5)),
            Reminders.key(2, LocalDate.of(2026, 11, 6)),
            Reminders.key(3, LocalDate.of(2026, 11, 9)),
            "broken"
        )
        assertEquals(
            setOf(Reminders.key(2, LocalDate.of(2026, 11, 6)), Reminders.key(3, LocalDate.of(2026, 11, 9))),
            Reminders.actualKeys(keys, today)
        )
    }
}
