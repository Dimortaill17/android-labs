package com.example.subtrack.data.repository

import android.content.Context
import com.example.subtrack.data.local.AppDatabase
import com.example.subtrack.data.local.toCategory
import com.example.subtrack.data.local.toEntity
import com.example.subtrack.domain.Categories
import com.example.subtrack.domain.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CategoryRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).categoryDao()
    private val subscriptionDao = AppDatabase.getInstance(context).subscriptionDao()

    fun getCategories(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { it.toCategory() } }
            .onEach { cachedCategories = it }
    }

    suspend fun saveCategory(category: Category) {
        dao.upsert(category.toEntity())
    }

    suspend fun deleteCategory(category: Category) {
        subscriptionDao.moveToCategory(category.id, Categories.OTHER_ID)
        dao.deleteById(category.id)
    }

    suspend fun resetCategories() {
        dao.deleteAll()
        dao.insertAll(Categories.defaults.map { it.toEntity() })
    }

    companion object {
        @Volatile
        var cachedCategories: List<Category>? = null
            private set
    }
}
