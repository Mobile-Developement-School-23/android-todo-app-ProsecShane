package com.prosecshane.todoapp.data.datasource

import com.prosecshane.todoapp.data.model.TodoItem

// Interface, that serves as a data source for the app. Gets a list of items
interface TodoItemsDataSource {
    suspend fun loadTodoItems(): List<TodoItem>
}
