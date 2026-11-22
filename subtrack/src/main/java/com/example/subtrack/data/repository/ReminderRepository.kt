package com.example.subtrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.remindersDataStore by preferencesDataStore(name = "reminders")

class ReminderRepository(private val context: Context) {
    suspend fun getSentReminders(): Set<String> {
        return context.remindersDataStore.data.first()[KEY_SENT].orEmpty()
    }

    suspend fun saveSentReminders(reminders: Set<String>) {
        context.remindersDataStore.edit { preferences ->
            preferences[KEY_SENT] = reminders
        }
    }

    suspend fun clear() {
        context.remindersDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_SENT = stringSetPreferencesKey("sent_reminders")
    }
}
