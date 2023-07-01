package com.prosecshane.todoapp.ui

import android.app.Application
import com.prosecshane.todoapp.ioc.ApplicationComponent
import java.util.concurrent.TimeUnit

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

class App : Application() {
    val applicationComponent by lazy { ApplicationComponent() }
}
