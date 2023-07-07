package com.prosecshane.todoapp.ioc

import com.prosecshane.todoapp.data.datasource.LocalDataSource
import com.prosecshane.todoapp.data.repository.TodoItemsRepository

// Application component - contains information to be used by the whole app
class ApplicationComponent {
    private val dataSource = LocalDataSource()
    private val todoItemsRepository = TodoItemsRepository(dataSource)
    val viewModelFactory = ViewModelFactory(todoItemsRepository)
}
