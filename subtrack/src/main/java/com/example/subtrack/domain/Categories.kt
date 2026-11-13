package com.example.subtrack.domain

import androidx.annotation.StringRes
import com.example.subtrack.R

object Categories {
    const val OTHER_ID = 11L

    val colors: List<Int> = listOf(
        0xFF5847EE.toInt(),
        0xFF3022BA.toInt(),
        0xFF6F0388.toInt(),
        0xFF6B28C4.toInt(),
        0xFF9139CF.toInt(),
        0xFFB749D6.toInt(),
        0xFF7C64FE.toInt(),
        0xFF7A8DFE.toInt(),
        0xFFB699FE.toInt(),
        0xFFE17FFE.toInt(),
        0xFFEAA6FF.toInt(),
        0xFFB2C0FE.toInt()
    )

    val defaults: List<Category> = listOf(
        Category(1, "Развлечения", "entertainment", colors[0]),
        Category(2, "Музыка", "music", colors[5]),
        Category(3, "Кино и сериалы", "movies", colors[2]),
        Category(4, "Видео", "video", colors[6]),
        Category(5, "Игры", "games", colors[10]),
        Category(6, "Облако", "cloud", colors[7]),
        Category(7, "Дизайн", "design", colors[9]),
        Category(8, "Работа", "work", colors[1]),
        Category(9, "Образование", "education", colors[8]),
        Category(10, "Здоровье и спорт", "health", colors[11]),
        Category(OTHER_ID, "Другое", "other", colors[3])
    )

    fun nextColor(usedColors: List<Int>): Int {
        return colors.minBy { color -> usedColors.count { it == color } }
    }

    @StringRes
    fun titleRes(titleKey: String): Int? {
        return when (titleKey) {
            "entertainment" -> R.string.category_entertainment
            "music" -> R.string.category_music
            "movies" -> R.string.category_movies
            "video" -> R.string.category_video
            "games" -> R.string.category_games
            "cloud" -> R.string.category_cloud
            "design" -> R.string.category_design
            "work" -> R.string.category_work
            "education" -> R.string.category_education
            "health" -> R.string.category_health
            "other" -> R.string.category_other
            else -> null
        }
    }
}
