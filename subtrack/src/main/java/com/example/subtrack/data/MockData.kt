package com.example.subtrack.data

import com.example.subtrack.data.utils.serviceColor
import com.example.subtrack.domain.Subscription
import java.time.LocalDate

object MockData {
    fun subscriptions(): List<Subscription> = listOf(
        Subscription(1, "Spotify", "Музыка", 169.0, false, LocalDate.of(2026, 1, 28), null, true, 3, "Музыка", 0xFF1DB954.toInt()),
        Subscription(2, "iCloud+", "Облако", 299.0, false, LocalDate.of(2026, 1, 10), null, true, 3, "200 ГБ", 0xFF38A8F6.toInt()),
        Subscription(3, "Netflix", "Развлечения", 699.0, false, LocalDate.of(2026, 2, 15), null, true, 3, "Кино и сериалы", 0xFFE50914.toInt()),
        Subscription(4, "Яндекс Плюс", "Развлечения", 399.0, false, LocalDate.of(2026, 5, 20), null, true, 3, "Кино и музыка", 0xFFFF2C86.toInt()),
        Subscription(5, "Canva Pro", "Дизайн", 259.0, false, LocalDate.of(2026, 6, 12), null, true, 3, "Дизайн", 0xFF7657F6.toInt()),
        Subscription(6, "Duolingo Super", "Образование", 3590.0, true, LocalDate.of(2026, 3, 5), null, true, 5, "Английский язык", serviceColor("Duolingo Super")),
        Subscription(7, "Kaspersky", "Другое", 1990.0, true, LocalDate.of(2025, 12, 14), null, true, 5, "Антивирус", serviceColor("Kaspersky"))
    )
}
