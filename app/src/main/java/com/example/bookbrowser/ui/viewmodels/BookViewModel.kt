package com.example.bookbrowser.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbrowser.config.DebugSettingsStore
import com.example.bookbrowser.data.model.BookItem
import com.example.bookbrowser.data.network.NetworkModule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _books = mutableStateOf<List<BookItem>>(emptyList())
    val books: State<List<BookItem>> = _books

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            DebugSettingsStore.bookQuery.collect { query ->
                loadBooks(query)
            }
        }
    }

    fun reloadBooks() {
        viewModelScope.launch {
            loadBooks(DebugSettingsStore.bookQuery.value)
        }
    }

    private suspend fun loadBooks(query: String) {
        try {
            _isLoading.value = true
            val response = NetworkModule.api.getBooks(query)
            _books.value = response.items
        } catch (_: Exception) {
            _books.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }
}
