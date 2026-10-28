package com.example.subtrack.data.repository

import android.content.Context
import com.example.subtrack.data.MockData
import com.example.subtrack.domain.Subscription
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate

class SubscriptionRepository(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        if (!prefs.getBoolean(KEY_SEEDED, false)) {
            saveAll(MockData.subscriptions())
            prefs.edit().putBoolean(KEY_SEEDED, true).apply()
        }
    }

    fun getSubscriptions(): List<Subscription> {
        val json = prefs.getString(KEY_SUBSCRIPTIONS, null) ?: return emptyList()
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { array.getJSONObject(it).toSubscription() }
        } catch (e: JSONException) {
            emptyList()
        }
    }

    fun saveSubscription(subscription: Subscription) {
        val list = getSubscriptions().toMutableList()
        val index = list.indexOfFirst { it.id == subscription.id }
        if (index >= 0) {
            list[index] = subscription
        } else {
            list.add(subscription)
        }
        saveAll(list)
    }

    fun deleteSubscription(id: Long) {
        saveAll(getSubscriptions().filter { it.id != id })
    }

    private fun saveAll(list: List<Subscription>) {
        val array = JSONArray()
        list.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_SUBSCRIPTIONS, array.toString()).apply()
    }

    companion object {
        private const val PREFS_NAME = "subscriptions_prefs"
        private const val KEY_SUBSCRIPTIONS = "subscriptions"
        private const val KEY_SEEDED = "seeded"
    }
}

private fun Subscription.toJson(): JSONObject {
    return JSONObject()
        .put("id", id)
        .put("name", name)
        .put("category", category)
        .put("price", price)
        .put("yearly", yearly)
        .put("startDate", startDate.toString())
        .put("endDate", endDate?.toString() ?: JSONObject.NULL)
        .put("remind", remind)
        .put("remindDays", remindDays)
        .put("note", note)
        .put("color", color)
}

private fun JSONObject.toSubscription(): Subscription {
    return Subscription(
        id = getLong("id"),
        name = getString("name"),
        category = getString("category"),
        price = getDouble("price"),
        yearly = getBoolean("yearly"),
        startDate = LocalDate.parse(getString("startDate")),
        endDate = if (isNull("endDate")) null else LocalDate.parse(getString("endDate")),
        remind = getBoolean("remind"),
        remindDays = getInt("remindDays"),
        note = optString("note", ""),
        color = getInt("color")
    )
}
