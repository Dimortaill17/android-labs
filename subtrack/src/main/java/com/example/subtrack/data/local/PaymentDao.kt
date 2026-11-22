package com.example.subtrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE subscriptionId = :subscriptionId")
    fun getBySubscription(subscriptionId: Long): Flow<List<PaymentEntity>>

    @Upsert
    suspend fun upsert(payment: PaymentEntity)

    @Query("DELETE FROM payments WHERE subscriptionId = :subscriptionId AND date = :date")
    suspend fun delete(subscriptionId: Long, date: LocalDate)

    @Query("DELETE FROM payments WHERE subscriptionId = :subscriptionId")
    suspend fun deleteBySubscription(subscriptionId: Long)

    @Query("DELETE FROM payments")
    suspend fun deleteAll()
}
