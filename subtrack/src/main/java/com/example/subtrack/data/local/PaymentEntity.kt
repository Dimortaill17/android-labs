package com.example.subtrack.data.local

import androidx.room.Entity
import com.example.subtrack.domain.Payment
import com.example.subtrack.domain.PaymentStatus
import java.time.LocalDate

@Entity(tableName = "payments", primaryKeys = ["subscriptionId", "date"])
data class PaymentEntity(
    val subscriptionId: Long,
    val date: LocalDate,
    val status: String
)

fun PaymentEntity.toPayment(): Payment {
    return Payment(
        subscriptionId = subscriptionId,
        date = date,
        status = PaymentStatus.valueOf(status)
    )
}
