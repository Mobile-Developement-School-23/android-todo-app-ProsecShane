package com.prosecshane.todoapp.ui

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.prosecshane.todoapp.data.Constants.WORKER_NAME
import com.prosecshane.todoapp.data.database.RegularSyncWorker
import com.prosecshane.todoapp.ioc.ApplicationComponent
import java.util.concurrent.TimeUnit

// Application instance
class App : Application() {
    val applicationComponent by lazy { ApplicationComponent() }

    override fun onCreate() {
        super.onCreate()
        setupRegularSync()
    }

    private fun setupRegularSync() {
        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest = PeriodicWorkRequestBuilder<RegularSyncWorker>(
            8, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(WORKER_NAME, ExistingPeriodicWorkPolicy.UPDATE, myWorkRequest)
    }
}
