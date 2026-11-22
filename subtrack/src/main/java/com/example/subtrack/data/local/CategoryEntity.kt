package com.example.subtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.subtrack.domain.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val titleKey: String?,
    val color: Int
)

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        titleKey = titleKey,
        color = color
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        titleKey = titleKey,
        color = color
    )
}
