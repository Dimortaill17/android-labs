package com.example.subtrack

import com.example.subtrack.domain.SortOrder
import com.example.subtrack.domain.Subscription
import com.example.subtrack.domain.SubscriptionSorter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class SubscriptionSorterTest {
    private val today = LocalDate.of(2026, 11, 6)

    private fun subscription(id: Long, name: String, price: Double, day: Int, categoryId: Long) = Subscription(
        id = id,
        name = name,
        categoryId = categoryId,
        price = price,
        yearly = false,
        startDate = LocalDate.of(2026, 11, day),
        endDate = null,
        remind = false,
        remindDays = 3,
        note = "",
        color = 0
    )

    private val subscriptions = listOf(
        subscription(1, "Netflix", 699.0, 15, 2),
        subscription(2, "Spotify", 169.0, 28, 1),
        subscription(3, "iCloud+", 299.0, 10, 2)
    )

    private fun ids(order: SortOrder, titles: Map<Long, String> = emptyMap()): List<Long> {
        return SubscriptionSorter.sort(subscriptions, order, today, titles).map { it.id }
    }

    @Test
    fun sortsByPrice() {
        assertEquals(listOf(1L, 3L, 2L), ids(SortOrder.PRICE_DESC))
        assertEquals(listOf(2L, 3L, 1L), ids(SortOrder.PRICE_ASC))
    }

    @Test
    fun sortsByNextPayment() {
        assertEquals(listOf(3L, 1L, 2L), ids(SortOrder.DATE_NEAR))
        assertEquals(listOf(2L, 1L, 3L), ids(SortOrder.DATE_FAR))
    }

    @Test
    fun sortsByNameIgnoringCase() {
        assertEquals(listOf(3L, 1L, 2L), ids(SortOrder.NAME))
    }

    @Test
    fun sortsByCategoryThenName() {
        val titles = mapOf(1L to "Музыка", 2L to "Кино")
        assertEquals(listOf(3L, 1L, 2L), ids(SortOrder.CATEGORY, titles))
    }

    @Test
    fun finishedSubscriptionsGoLastByNearestPayment() {
        val finished = subscription(4, "Old", 100.0, 1, 1).copy(
            startDate = LocalDate.of(2026, 1, 1),
            endDate = LocalDate.of(2026, 3, 1)
        )
        val sorted = SubscriptionSorter.sort(subscriptions + finished, SortOrder.DATE_NEAR, today)
        assertEquals(4L, sorted.last().id)
    }
}
