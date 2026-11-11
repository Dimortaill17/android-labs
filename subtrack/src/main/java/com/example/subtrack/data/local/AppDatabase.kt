package com.example.subtrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.subtrack.data.MockData
import com.example.subtrack.domain.Categories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SubscriptionEntity::class, CategoryEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "subtrack.db")
                .fallbackToDestructiveMigration(true)
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        fillDatabase(context)
                    }
                })
                .build()
        }

        private fun fillDatabase(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                if (database.categoryDao().count() == 0) {
                    database.categoryDao().insertAll(Categories.defaults.map { it.toEntity() })
                    database.subscriptionDao().insertAll(MockData.subscriptions().map { it.toEntity() })
                }
            }
        }
    }
}
