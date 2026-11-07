package com.example.subtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.subtrack.domain.Subscription
import java.time.LocalDate

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val yearly: Boolean,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val remind: Boolean,
    val remindDays: Int,
    val note: String,
    val color: Int
)

fun SubscriptionEntity.toSubscription(): Subscription {
    return Subscription(
        id = id,
        name = name,
        category = category,
        price = price,
        yearly = yearly,
        startDate = startDate,
        endDate = endDate,
        remind = remind,
        remindDays = remindDays,
        note = note,
        color = color
    )
}

fun Subscription.toEntity(): SubscriptionEntity {
    return SubscriptionEntity(
        id = id,
        name = name,
        category = category,
        price = price,
        yearly = yearly,
        startDate = startDate,
        endDate = endDate,
        remind = remind,
        remindDays = remindDays,
        note = note,
        color = color
    )
}
