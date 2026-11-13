package com.example.subtrack.domain

data class PaymentHistoryData(
    val subscription: Subscription,
    val payments: List<Payment>
)
