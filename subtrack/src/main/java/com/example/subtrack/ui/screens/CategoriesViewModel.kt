package com.example.subtrack.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.subtrack.data.repository.CategoryRepository
import com.example.subtrack.domain.Category
import com.example.subtrack.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CategoryRepository(application)

    private val _state = MutableStateFlow<UiState<List<Category>>>(
        CategoryRepository.cachedCategories?.let { UiState.Success(it) } ?: UiState.Loading
    )
    val state: StateFlow<UiState<List<Category>>> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (_state.value is UiState.Error) {
                _state.value = UiState.Loading
            }
            repository.getCategories()
                .catch { _state.value = UiState.Error(it.message.orEmpty()) }
                .collect { _state.value = UiState.Success(it) }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun saveCategory(category: Category) {
        viewModelScope.launch {
            repository.saveCategory(category)
        }
    }
}
