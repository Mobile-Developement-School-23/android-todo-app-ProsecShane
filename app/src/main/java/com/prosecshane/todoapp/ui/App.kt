package com.prosecshane.todoapp.ui

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.ioc.ApplicationComponent

// Application instance
@RequiresApi(Build.VERSION_CODES.M)
class App : Application() {
    private val applicationComponent = ApplicationComponent()

    init { instance = this }

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
