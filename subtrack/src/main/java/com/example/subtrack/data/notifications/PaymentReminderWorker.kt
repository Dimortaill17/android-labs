package com.example.subtrack.data.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.subtrack.data.repository.ReminderRepository
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.data.utils.withLocale
import com.example.subtrack.domain.Reminders
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.Locale

class PaymentReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val settings = SettingsRepository(applicationContext).settings.first()
        val subscriptions = SubscriptionRepository(applicationContext).getSubscriptions().first()
        val reminderRepository = ReminderRepository(applicationContext)
        val localizedContext = applicationContext.withLocale(Locale.forLanguageTag(settings.language))
        val today = LocalDate.now()
        val sentReminders = Reminders.actualKeys(reminderRepository.getSentReminders(), today).toMutableSet()

        PaymentNotifications.createChannel(localizedContext)
        subscriptions
            .filter { it.shouldRemind(today) }
            .forEach { subscription ->
                val paymentDate = subscription.nextPayment(today) ?: return@forEach
                val key = Reminders.key(subscription.id, paymentDate)
                if (key !in sentReminders && PaymentNotifications.show(localizedContext, subscription, settings.hideAmounts)) {
                    sentReminders.add(key)
                }
            }
        reminderRepository.saveSentReminders(sentReminders)

        return Result.success()
    }
}
