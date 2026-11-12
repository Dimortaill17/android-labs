package com.example.subtrack.data.repository

import android.content.Context
import com.example.subtrack.data.local.AppDatabase
import com.example.subtrack.data.local.PaymentEntity
import com.example.subtrack.data.local.toPayment
import com.example.subtrack.domain.PaymentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class PaymentRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).paymentDao()

    fun getMarks(subscriptionId: Long): Flow<Map<LocalDate, PaymentStatus>> {
        return dao.getBySubscription(subscriptionId).map { list ->
            list.map { it.toPayment() }.associate { it.date to it.status }
        }
    }

    suspend fun setStatus(subscriptionId: Long, date: LocalDate, status: PaymentStatus?) {
        if (status == null) {
            dao.delete(subscriptionId, date)
        } else {
            dao.upsert(PaymentEntity(subscriptionId, date, status.name))
        }
    }
}
