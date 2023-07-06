package com.prosecshane.todoapp.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prosecshane.todoapp.data.datasource.LocalDataSource
import com.prosecshane.todoapp.data.datasource.TodoItemsDataSource
import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository class to store items
class TodoItemsRepository(
    // Data source that updates the items
    private val dataSource: TodoItemsDataSource
) {
    // Live data of lists, that store the items (with public getter)
    private val _todoItems = MutableLiveData<List<TodoItem>>(emptyList())
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    // Update the items
    @MainThread
    suspend fun updateTodoItems() {
       withContext(Dispatchers.IO) {
           val loadedTodoItems = dataSource.loadTodoItems()
           _todoItems.value = loadedTodoItems
       }
    }

    // Change an item
    suspend fun changeTodoItem(changedTodoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            val fullyChangedTodoItem = changedTodoItem.copy(editedOn = System.currentTimeMillis())
            val newTodoItems = todoItems.value.orEmpty().map { todoItem ->
                if (todoItem.id == changedTodoItem.id) fullyChangedTodoItem
                else todoItem
            }
            _todoItems.value = newTodoItems
            if (dataSource is LocalDataSource) {
                dataSource.database.todoItems().changeTodoItem(fullyChangedTodoItem)
            }
        }
    }

    // Delete an item
    suspend fun deleteTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            _todoItems.postValue(todoItems.value.orEmpty().filter { it.id != todoItem.id })
            if (dataSource is LocalDataSource) {
                dataSource.database.todoItems().deleteTodoItem(todoItem)
            }
        }
    }

    // Add an item
    suspend fun addTodoItem(newTodoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            val appended = todoItems.value.orEmpty() as MutableList<TodoItem>
            appended.add(newTodoItem)
            _todoItems.postValue(appended)
            if (dataSource is LocalDataSource) {
                dataSource.database.todoItems().addTodoItem(newTodoItem)
            }
        }
    }
}
