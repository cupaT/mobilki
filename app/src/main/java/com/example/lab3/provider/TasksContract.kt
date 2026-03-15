package com.example.lab3.provider

import android.net.Uri
import android.provider.BaseColumns

object TasksContract {
    const val AUTHORITY = "com.example.lab3.provider"
    const val PATH_TASKS = "tasks"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_TASKS")

    object Columns : BaseColumns {
        const val TITLE = "title"
        const val COMPLETED = "completed"
        const val REMOTE_ID = "remote_id"
        const val UPDATED_AT = "updated_at"
    }
}
