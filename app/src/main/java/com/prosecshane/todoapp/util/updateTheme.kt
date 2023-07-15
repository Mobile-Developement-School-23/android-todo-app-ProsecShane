package com.prosecshane.todoapp.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil

fun updateTheme(context: Context) {
    AppCompatDelegate.setDefaultNightMode(
        when (SharedPreferencesUtil(context).get(
            SharedPreferencesConstants.SETTINGS_THEME, 2
        ) as Int) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}
