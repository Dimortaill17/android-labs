package com.example.subtrack.data.utils

private val serviceColors = listOf(
    0xFF1DB954,
    0xFF38A8F6,
    0xFF7657F6,
    0xFFE50914,
    0xFF10A37F,
    0xFFFF2C86,
    0xFFFF7A00,
    0xFF0EA5E9,
    0xFFF59E0B,
    0xFF6366F1
)

fun serviceColor(name: String): Int {
    val index = name.lowercase().hashCode().mod(serviceColors.size)
    return serviceColors[index].toInt()
}
