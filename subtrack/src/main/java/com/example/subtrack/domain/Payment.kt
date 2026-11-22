package com.example.subtrack.domain

import java.time.LocalDate

enum class PaymentStatus {
    PAID,
    FAILED,
    MISSED,
    UPCOMING
}

data class Payment(
    val subscriptionId: Long,
    val date: LocalDate,
    val status: PaymentStatus
)
