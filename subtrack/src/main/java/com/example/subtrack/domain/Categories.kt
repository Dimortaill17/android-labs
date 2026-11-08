package com.example.subtrack.domain

import androidx.annotation.StringRes
import com.example.subtrack.R

object Categories {
    private val titles: Map<String, Int> = mapOf(
        "Развлечения" to R.string.category_entertainment,
        "Музыка" to R.string.category_music,
        "Кино и сериалы" to R.string.category_movies,
        "Видео" to R.string.category_video,
        "Игры" to R.string.category_games,
        "Облако" to R.string.category_cloud,
        "Дизайн" to R.string.category_design,
        "Работа" to R.string.category_work,
        "Образование" to R.string.category_education,
        "Здоровье и спорт" to R.string.category_health,
        "Другое" to R.string.category_other
    )

    val list: List<String> = titles.keys.toList()

    @StringRes
    fun title(category: String): Int {
        return titles[category] ?: R.string.category_other
    }
}
