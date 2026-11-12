package com.example.subtrack.domain

import java.time.LocalDate

object PaymentHistory {
    fun build(
        subscription: Subscription,
        marks: Map<LocalDate, PaymentStatus>,
        today: LocalDate = LocalDate.now()
    ): List<Payment> {
        val lastDate = subscription.nextPayment(today) ?: LocalDate.MAX
        return subscription.paymentsUntil(lastDate)
            .map { date ->
                val status = marks[date] ?: if (date.isBefore(today)) PaymentStatus.MISSED else PaymentStatus.UPCOMING
                Payment(subscription.id, date, status)
            }
            .reversed()
    }
}
