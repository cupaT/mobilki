package com.example.lab3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab3.data.local.TaskEntity
import com.example.lab3.sync.SyncScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as ToDoApplication
    private val repository = app.repository

    val tasks: StateFlow<List<TaskEntity>> = repository.observeTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _lastSync = MutableStateFlow(repository.getLastSyncTime())
    val lastSync: StateFlow<Long> = _lastSync

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addLocalTask(title.trim())
        }
    }

    fun toggleTaskCompleted(taskId: Long) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(taskId)
        }
    }

    fun triggerSyncNow() {
        SyncScheduler.enqueueImmediate(getApplication())
        _uiMessage.update { "Sync started" }
    }

    fun refreshLastSync() {
        _lastSync.update { repository.getLastSyncTime() }
    }

    fun messageShown() {
        _uiMessage.update { null }
    }
}
