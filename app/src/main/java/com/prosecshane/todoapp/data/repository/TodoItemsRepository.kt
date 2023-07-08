package com.prosecshane.todoapp.data.repository

import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prosecshane.todoapp.data.datasource.LocalDataSource
import com.prosecshane.todoapp.data.datasource.TodoItemsDataSource
import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository class to store items
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemsRepository(
    // Data source that updates the items
    private val dataSource: TodoItemsDataSource
) {
    // Live data of lists, that store the items (with public getter)
    private val _todoItems = MutableLiveData<List<TodoItem>>(emptyList())
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    // Update the items (in Main Thread)
    @MainThread
    suspend fun updateTodoItems() {
        val loadedTodoItems = withContext(Dispatchers.IO) { dataSource.loadTodoItems() }
        _todoItems.value = loadedTodoItems
    }

    // Change an item (in Background)
    suspend fun changeTodoItem(changedTodoItem: TodoItem) {
        val fullyChangedTodoItem = changedTodoItem.copy(editedOn = System.currentTimeMillis())
        val newTodoItems = withContext(Dispatchers.Default) {
            todoItems.value.orEmpty().map { todoItem ->
                if (todoItem.id == changedTodoItem.id) fullyChangedTodoItem
                else todoItem
            }
        }
        _todoItems.value = newTodoItems
        if (dataSource is LocalDataSource) {
            withContext(Dispatchers.Default) {
                dataSource.database.todoItems().changeTodoItem(changedTodoItem)
            }
        }
    }

    // Delete an item (in Background)
    suspend fun deleteTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            _todoItems.postValue(todoItems.value.orEmpty().filter { it.id != todoItem.id })
        }
        if (dataSource is LocalDataSource) {
            withContext(Dispatchers.Default) {
                dataSource.database.todoItems().deleteTodoItem(todoItem)
            }
        }
    }

    // Add an item (in Background)
    suspend fun addTodoItem(newTodoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            _todoItems.postValue(todoItems.value.orEmpty() + listOf(newTodoItem))
        }
        if (dataSource is LocalDataSource) {
            withContext(Dispatchers.Default) {
                dataSource.database.todoItems().addTodoItem(newTodoItem)
            }
        }
    }
}
