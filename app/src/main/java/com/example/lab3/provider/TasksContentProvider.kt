package com.example.lab3.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Binder
import android.os.Process
import com.example.lab3.ToDoApplication
import com.example.lab3.data.local.TaskEntity
import com.example.lab3.notifications.NotificationHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TasksContentProvider : ContentProvider() {
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(TasksContract.AUTHORITY, TasksContract.PATH_TASKS, TASKS)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        if (uriMatcher.match(uri) != TASKS) {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
        val app = context?.applicationContext as? ToDoApplication
            ?: throw IllegalStateException("Application is not available")
        val tasks = runBlocking { app.repository.observeTasks().first() }

        val columns = arrayOf(
            "_id",
            TasksContract.Columns.TITLE,
            TasksContract.Columns.COMPLETED,
            TasksContract.Columns.REMOTE_ID,
            TasksContract.Columns.UPDATED_AT
        )
        val cursor = MatrixCursor(columns)
        tasks.forEach { task ->
            cursor.addRow(
                arrayOf(
                    task.id,
                    task.title,
                    if (task.completed) 1 else 0,
                    task.remoteId,
                    task.updatedAt
                )
            )
        }
        cursor.setNotificationUri(context?.contentResolver, TasksContract.CONTENT_URI)
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.dir/vnd.${TasksContract.AUTHORITY}.${TasksContract.PATH_TASKS}"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (uriMatcher.match(uri) != TASKS) {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
        val title = values?.getAsString(TasksContract.Columns.TITLE)?.trim().orEmpty()
        if (title.isBlank()) {
            throw IllegalArgumentException("Task title is required")
        }
        val completed = (values?.getAsInteger(TasksContract.Columns.COMPLETED) ?: 0) == 1
        val app = context?.applicationContext as? ToDoApplication
            ?: throw IllegalStateException("Application is not available")

        val insertedId = runBlocking {
            app.repository.addTaskFromContentProvider(title, completed)
        }
        context?.contentResolver?.notifyChange(TasksContract.CONTENT_URI, null)
        if (Binder.getCallingUid() != Process.myUid()) {
            NotificationHelper.showExternalTaskAdded(context ?: return Uri.EMPTY, title)
        }
        return ContentUris.withAppendedId(TasksContract.CONTENT_URI, insertedId)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    companion object {
        private const val TASKS = 1
    }
}
