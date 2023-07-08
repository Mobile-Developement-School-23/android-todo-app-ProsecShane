package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.data.datasource.LocalDataSource
import com.prosecshane.todoapp.data.datasource.TodoItemsDataSource
import com.prosecshane.todoapp.data.repository.TodoItemsRepository

// Application component - contains information to be used by the whole app
@RequiresApi(Build.VERSION_CODES.M)
class ApplicationComponent {
    private val dataSource by lazy { LocalDataSource() }

    fun getDataSource(): TodoItemsDataSource = dataSource

    fun mainActivityComponent(): MainActivityComponent =
        MainActivityComponent(this)
}
