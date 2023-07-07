package com.prosecshane.todoapp.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.prosecshane.todoapp.R
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

        mainActivityComponent = App.getApplicationComponent().mainActivityComponent()
        mainActivityComponent.inject(this)
    }
}
