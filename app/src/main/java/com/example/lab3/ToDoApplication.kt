package com.example.lab3

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.lab3.data.TasksRepository
import com.example.lab3.data.local.AppDatabase
import com.example.lab3.data.remote.ApiFactory
import com.example.lab3.notifications.NotificationHelper
import com.example.lab3.sync.ConnectivityReceiver
import com.example.lab3.sync.SyncScheduler

class ToDoApplication : Application() {
    lateinit var repository: TasksRepository
        private set

    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = TasksRepository(this, database.taskDao(), ApiFactory.api)

        NotificationHelper.createChannels(this)
        SyncScheduler.schedulePeriodic(this)

        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(connectivityReceiver)
    }
}
