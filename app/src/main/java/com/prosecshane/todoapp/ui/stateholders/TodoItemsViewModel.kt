package com.prosecshane.todoapp.ui.stateholders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.launch

// View Model, contains relevant information
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemsViewModel(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {
    // Show onyl undone or show all
    val _onlyUndone = MutableLiveData(false)
    val onlyUndone: LiveData<Boolean> = _onlyUndone

    // Currently working item (for TodoItemFragment)
    val _currentTodoItem = MutableLiveData(TodoItem(
        id = "ERROR_ITEM", done = false,
        text = "If you see this, an error occurred. Report this bug to the developer",
        importance = Importance.HIGH, deadline = null
    ))
    val currentTodoItem: LiveData<TodoItem> = _currentTodoItem

    // LiveData from the repository
    val todoItems: LiveData<List<TodoItem>> = todoItemsRepository.todoItems

    // How much is done (for the ListFragment)
    val _doneAmount = MutableLiveData(0)
    val doneAmount: LiveData<Int> = _doneAmount

    // On start: update items and amount of done
    init {
        updateTodoItems()
        updateDoneAmount()
    }

    // Update the LiveData with amount
    fun updateDoneAmount(todoItemsList: List<TodoItem> = (todoItems.value as List<TodoItem>)) {
        _doneAmount.value = todoItemsList.filter { it.done }.size
    }

    // Flip the switch of visibility
    fun switchVisible() {
        viewModelScope.launch {
            val visibility = onlyUndone.value as Boolean
            _onlyUndone.value = !visibility
        }
    }

    // Set the current working item for 2nd Fragment
    fun setCurrentTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            _currentTodoItem.value = todoItem
        }
    }

    // Load items to repo from datasource
    fun updateTodoItems() {
        viewModelScope.launch {
            todoItemsRepository.updateTodoItems()
            updateDoneAmount()
        }
    }

    // Update certain item
    fun onTodoItemChanged(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.changeTodoItem(todoItem)
            updateDoneAmount()
        }
    }

    // Delete certain item
    fun onTodoItemDeleted(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.deleteTodoItem(todoItem.id)
            updateDoneAmount()
        }
    }

    // Add an item
    fun onTodoItemAdded(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.addTodoItem(todoItem)
            updateDoneAmount()
        }
    }
}
