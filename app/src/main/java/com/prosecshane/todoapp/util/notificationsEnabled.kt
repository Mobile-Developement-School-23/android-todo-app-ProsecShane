package com.prosecshane.todoapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.SETTINGS_NOTIFICATIONS
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil

fun notificationsEnabled(context: Context): Boolean = SharedPreferencesUtil(context).get(
    SETTINGS_NOTIFICATIONS, false
) as Boolean
