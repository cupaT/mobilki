package com.example.lab3.data

import android.content.Context
import com.example.lab3.data.local.TaskDao
import com.example.lab3.data.local.TaskEntity
import com.example.lab3.data.remote.CreateTodoRequest
import com.example.lab3.data.remote.JsonPlaceholderApi
import kotlinx.coroutines.flow.Flow

class TasksRepository(
    private val context: Context,
    private val taskDao: TaskDao,
    private val api: JsonPlaceholderApi
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun observeTasks(): Flow<List<TaskEntity>> = taskDao.observeAll()

    fun getLastSyncTime(): Long = prefs.getLong(KEY_LAST_SYNC, 0L)

    suspend fun addLocalTask(title: String, completed: Boolean = false): Long {
        return taskDao.insert(
            TaskEntity(
                title = title,
                completed = completed,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addTaskFromContentProvider(title: String, completed: Boolean = false): Long {
        return addLocalTask(title, completed)
    }

    suspend fun toggleTaskCompleted(taskId: Long) {
        val task = taskDao.getById(taskId) ?: return
        taskDao.update(
            task.copy(
                completed = !task.completed,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun sync(): Int {
        var affected = 0
        val remoteTodos = api.getTodos().take(50)
        for (remote in remoteTodos) {
            val existing = taskDao.getByRemoteId(remote.id)
            if (existing == null) {
                taskDao.insert(
                    TaskEntity(
                        title = remote.title,
                        completed = remote.completed,
                        remoteId = remote.id,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                affected++
            } else {
                taskDao.update(
                    existing.copy(
                        title = remote.title,
                        completed = remote.completed,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                affected++
            }
        }

        val unsynced = taskDao.getUnsynced()
        for (task in unsynced) {
            val created = api.createTodo(
                CreateTodoRequest(
                    title = task.title,
                    completed = task.completed
                )
            )
            taskDao.update(
                task.copy(
                    remoteId = created.id,
                    updatedAt = System.currentTimeMillis()
                )
            )
            affected++
        }

        prefs.edit().putLong(KEY_LAST_SYNC, System.currentTimeMillis()).apply()
        return affected
    }

    companion object {
        private const val PREFS_NAME = "sync_prefs"
        private const val KEY_LAST_SYNC = "last_sync_time"
    }
}
