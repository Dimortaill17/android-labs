package com.example.subtrack.data.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    private const val DAILY_WORK = "payment_reminders_daily"
    private const val CHECK_WORK = "payment_reminders_check"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<PaymentReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun checkNow(context: Context) {
        val request = OneTimeWorkRequestBuilder<PaymentReminderWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            CHECK_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
