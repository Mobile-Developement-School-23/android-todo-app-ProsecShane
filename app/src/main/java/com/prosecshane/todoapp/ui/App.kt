package com.prosecshane.todoapp.ui

import android.app.Application
import com.prosecshane.todoapp.ioc.ApplicationComponent

// Application instance
class App : Application() {
    val applicationComponent by lazy { ApplicationComponent() }
}
