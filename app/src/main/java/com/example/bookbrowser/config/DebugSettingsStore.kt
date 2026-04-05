package com.example.bookbrowser.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DebugSettingsStore {
    private val _bookQuery = MutableStateFlow(BuildConfigHelper.defaultBookQuery)
    val bookQuery: StateFlow<String> = _bookQuery.asStateFlow()

    fun updateBookQuery(query: String) {
        _bookQuery.value = query.ifBlank { BuildConfigHelper.defaultBookQuery }
    }

    fun reset() {
        _bookQuery.value = BuildConfigHelper.defaultBookQuery
    }
}
