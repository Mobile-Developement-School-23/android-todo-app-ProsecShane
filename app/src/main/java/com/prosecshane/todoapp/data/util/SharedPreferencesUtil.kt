package com.prosecshane.todoapp.data.util

import android.content.Context
import androidx.core.content.edit
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.SHARED_PREFS_FILENAME

class SharedPreferencesUtil(context: Context) {
    private val prefs = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)

    // TODO: Initialize prefs as xml?

    fun set(key: String, value: Any) {
        prefs.edit(commit = true) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                else -> throw ClassNotFoundException(
                    "Type ${value::class.java.simpleName} not supported for SharedPreferencesUtil"
                )
            }
        }
    }

    fun <T> get(key: String, fallback: T) = when (fallback) {
        is Boolean -> prefs.getBoolean(key, fallback)
        is Int -> prefs.getInt(key, fallback)
        is Long -> prefs.getLong(key, fallback)
        is Float -> prefs.getFloat(key, fallback)
        is String -> prefs.getString(key, fallback)
        else -> throw ClassNotFoundException(
            "Type ${fallback!!::class.java.simpleName} not supported for SharedPreferencesUtil"
        )
    }
}
