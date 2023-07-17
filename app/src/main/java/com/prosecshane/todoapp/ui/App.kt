package com.prosecshane.todoapp.ui

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil
import com.prosecshane.todoapp.ioc.ApplicationComponent
import com.prosecshane.todoapp.util.updateTheme

// Application instance
@RequiresApi(Build.VERSION_CODES.M)
class App : Application() {
    private val applicationComponent = ApplicationComponent()

    init { instance = this }

    override fun onCreate() {
        super.onCreate()
        updateTheme(applicationContext)
    }

    companion object {
        private var instance: App? = null

        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }

        fun getApplicationComponent() : ApplicationComponent {
            return instance!!.applicationComponent
        }
    }
}
