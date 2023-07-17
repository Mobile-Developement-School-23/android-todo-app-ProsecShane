package com.prosecshane.todoapp.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.NavController
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.SETTINGS_THEME
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil
import com.prosecshane.todoapp.ioc.MainActivityComponent
import com.prosecshane.todoapp.ui.App
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel

// Main Activity, has the Fragment Container
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    lateinit var mainActivityComponent: MainActivityComponent
    lateinit var navController: NavController
    lateinit var viewModel: TodoItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Getting rid of the top bar
        supportActionBar?.hide()

        // Dependency Injection
        mainActivityComponent = App.getApplicationComponent().mainActivityComponent()
        mainActivityComponent.inject(this)
    }
}
