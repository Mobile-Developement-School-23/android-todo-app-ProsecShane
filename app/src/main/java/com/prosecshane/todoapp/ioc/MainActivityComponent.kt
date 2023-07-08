package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import com.prosecshane.todoapp.ui.activities.MainActivity
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel

// Component that follows the Main Activity
@RequiresApi(Build.VERSION_CODES.M)
class MainActivityComponent(
    private val appComponent: ApplicationComponent,
) {
    private val todoItemsRepository by lazy { TodoItemsRepository(appComponent.getDataSource()) }
    val viewModel by lazy { TodoItemsViewModel(todoItemsRepository) }

    fun listFragmentComponent(): ListFragmentComponent =
        ListFragmentComponent(this)
    fun todoItemFragmentComponent(): TodoItemFragmentComponent =
        TodoItemFragmentComponent(this)

    fun inject(instance: MainActivity) {
        val navHostFragment = instance.supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as NavHostFragment
        instance.navController = navHostFragment.navController
        instance.viewModel = viewModel
    }
}
