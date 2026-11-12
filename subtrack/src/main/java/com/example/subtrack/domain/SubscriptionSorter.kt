package com.example.subtrack.domain

import java.time.LocalDate

object SubscriptionSorter {
    fun sort(
        subscriptions: List<Subscription>,
        order: SortOrder,
        today: LocalDate,
        categoryTitles: Map<Long, String> = emptyMap()
    ): List<Subscription> {
        val farFuture = LocalDate.MAX
        return when (order) {
            SortOrder.PRICE_DESC -> subscriptions.sortedByDescending { it.price }
            SortOrder.PRICE_ASC -> subscriptions.sortedBy { it.price }
            SortOrder.DATE_NEAR -> subscriptions.sortedBy { it.nextPayment(today) ?: farFuture }
            SortOrder.DATE_FAR -> subscriptions.sortedByDescending { it.nextPayment(today) ?: LocalDate.MIN }
            SortOrder.NAME -> subscriptions.sortedBy { it.name.lowercase() }
            SortOrder.CATEGORY -> subscriptions.sortedWith(
                compareBy<Subscription> { categoryTitles[it.categoryId].orEmpty().lowercase() }
                    .thenBy { it.name.lowercase() }
            )
        }
    }
}
