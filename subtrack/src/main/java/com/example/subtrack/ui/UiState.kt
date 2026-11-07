package com.example.subtrack.ui

import kotlin.coroutines.cancellation.CancellationException

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

suspend fun <T> loadUiState(request: suspend () -> T): UiState<T> =
    try {
        UiState.Success(request())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        UiState.Error(e.message ?: "Неизвестная ошибка")
    }
