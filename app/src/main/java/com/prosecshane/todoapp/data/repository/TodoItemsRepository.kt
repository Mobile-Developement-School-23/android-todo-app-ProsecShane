package com.prosecshane.todoapp.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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

    // Update the items (in Main Thread)
    @MainThread
    suspend fun updateTodoItems() {
        val loadedTodoItems = withContext(Dispatchers.IO) { dataSource.loadTodoItems() }
        _todoItems.value = loadedTodoItems
    }

    // Change an item (in Background)
    suspend fun changeTodoItem(changedTodoItem: TodoItem) {
        val newTodoItems = withContext(Dispatchers.Default) {
            todoItems.value.orEmpty().map { todoItem ->
                if (todoItem.id == changedTodoItem.id) changedTodoItem
                else todoItem.copy(editedOn = System.currentTimeMillis())
            }
        }
        _todoItems.value = newTodoItems
    }

    // Delete an item (in Background)
    suspend fun deleteTodoItem(todoItemId: String) {
        withContext(Dispatchers.Default) {
            _todoItems.postValue(todoItems.value.orEmpty().filter { it.id != todoItemId })
        }
    }

    // Add an item (in Background)
    suspend fun addTodoItem(newTodoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            val appended = todoItems.value.orEmpty() as MutableList<TodoItem>
            appended.add(newTodoItem)
            _todoItems.postValue(appended)
        }
    }
}
