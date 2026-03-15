package com.example.lab3.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.lab3.R

object NotificationHelper {
    const val SYNC_CHANNEL_ID = "sync_channel"
    const val EXTERNAL_TASK_CHANNEL_ID = "external_task_channel"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)

        val syncChannel = NotificationChannel(
            SYNC_CHANNEL_ID,
            "Sync events",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val externalChannel = NotificationChannel(
            EXTERNAL_TASK_CHANNEL_ID,
            "External tasks",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(syncChannel)
        manager.createNotificationChannel(externalChannel)
    }

    fun showSyncCompleted(context: Context, changed: Int) {
        if (!canNotify(context)) return
        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Синхронизация завершена")
            .setContentText("Обновлено записей: $changed")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(1001, notification)
    }

    fun showExternalTaskAdded(context: Context, title: String) {
        if (!canNotify(context)) return
        val notification = NotificationCompat.Builder(context, EXTERNAL_TASK_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Задача добавлена извне")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(1002, notification)
    }

    private fun canNotify(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
