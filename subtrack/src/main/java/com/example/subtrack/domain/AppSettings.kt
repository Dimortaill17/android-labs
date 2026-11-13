package com.example.subtrack.domain

data class AppSettings(
    val theme: String,
    val language: String,
    val sortOrder: SortOrder,
    val hideAmounts: Boolean
)
