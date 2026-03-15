package com.example.lab3.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lab3.data.TasksRepository
import com.example.lab3.data.local.AppDatabase
import com.example.lab3.data.remote.ApiFactory
import com.example.lab3.notifications.NotificationHelper

class SyncTasksWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val repository = TasksRepository(
                applicationContext,
                AppDatabase.getInstance(applicationContext).taskDao(),
                ApiFactory.api
            )
            val changed = repository.sync()
            NotificationHelper.showSyncCompleted(applicationContext, changed)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed at attempt=$runAttemptCount", e)
            if (runAttemptCount >= 2) Result.failure() else Result.retry()
        }
    }

    companion object {
        private const val TAG = "SyncTasksWorker"
    }
}
