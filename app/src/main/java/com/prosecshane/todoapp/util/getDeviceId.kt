package com.prosecshane.todoapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

// Device ID getter
@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String = Settings.Secure.getString(
    context.contentResolver, Settings.Secure.ANDROID_ID
)
