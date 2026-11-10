package com.example.subtrack.data.repository

import android.content.Context
import com.example.subtrack.data.local.AppDatabase
import com.example.subtrack.data.local.toEntity
import com.example.subtrack.data.local.toSubscription
import com.example.subtrack.domain.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SubscriptionRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).subscriptionDao()

    fun getSubscriptions(): Flow<List<Subscription>> {
        return dao.getAll()
            .map { list -> list.map { it.toSubscription() } }
            .onEach { cachedSubscriptions = it }
    }

    suspend fun getSubscription(id: Long): Subscription? {
        return dao.getById(id)?.toSubscription()
    }

    suspend fun saveSubscription(subscription: Subscription) {
        dao.upsert(subscription.toEntity())
    }

    suspend fun deleteSubscription(id: Long) {
        dao.deleteById(id)
    }

    suspend fun deleteAllSubscriptions() {
        dao.deleteAll()
    }

    companion object {
        @Volatile
        var cachedSubscriptions: List<Subscription>? = null
            private set
    }
}
