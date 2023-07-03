package com.prosecshane.todoapp.data.database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prosecshane.todoapp.data.Constants.CONNECTED
import com.prosecshane.todoapp.data.Constants.SHARED_PREFERENCES
import com.prosecshane.todoapp.data.repository.TodoItemsRepository

class RegularSyncWorker (
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        try {
            if (context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    .getBoolean(CONNECTED, false)) {
                TodoItemsRepository.setupSyncing(context)
                TodoItemsRepository.sync()
            }
        } catch (_: Exception) { return Result.retry() }
        finally { TodoItemsRepository.destroy() }
        return Result.success()
    }
}
