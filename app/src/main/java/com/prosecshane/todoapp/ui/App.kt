package com.prosecshane.todoapp.ui

import android.app.Application
import android.content.Context
import com.prosecshane.todoapp.ioc.ApplicationComponent

// Application instance
class App : Application() {
    private val applicationComponent = ApplicationComponent()

    init { instance = this }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun applicationComponent() : ApplicationComponent {
            return instance!!.applicationComponent
        }
    }
}
