package com.prosecshane.todoapp.ui.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.launch

// ViewModel, contains
class TodoItemsViewModel(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {
    // TODO: get from shared preferences
    // TODO: save to shared preferences
    val _onlyUndone = MutableLiveData(false)
    val onlyUndone: LiveData<Boolean> = _onlyUndone

    val _currentTodoItem = MutableLiveData(TodoItem(
        id = "ERROR_ITEM", done = false,
        text = "If you see this, an error occurred. Report this bug to the developer",
        importance = Importance.HIGH, deadline = null
    ))
    val currentTodoItem: LiveData<TodoItem> = _currentTodoItem

    val todoItems: LiveData<List<TodoItem>> = todoItemsRepository.todoItems

    val _doneAmount = MutableLiveData(0)
    val doneAmount: LiveData<Int> = _doneAmount

    init {
        updateTodoItems()
        updateDoneAmount()
    }

    fun updateDoneAmount(todoItemsList: List<TodoItem> = (todoItems.value as List<TodoItem>)) {
        _doneAmount.value = todoItemsList.filter { it.done }.size
    }

    fun switchVisible() {
        viewModelScope.launch {
            val visibility = onlyUndone.value as Boolean
            _onlyUndone.value = !visibility
        }
    }

    fun setCurrentTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            _currentTodoItem.value = todoItem
        }
    }

    fun updateTodoItems() {
        viewModelScope.launch {
            todoItemsRepository.updateTodoItems()
            updateDoneAmount()
        }
    }

    fun onTodoItemChanged(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.changeTodoItem(todoItem)
            updateDoneAmount()
            // TODO: save changes locally
        }
    }

    fun onTodoItemDeleted(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.deleteTodoItem(todoItem.id)
            updateDoneAmount()
            // TODO: save changes locally
        }
    }

    fun onTodoItemAdded(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.addTodoItem(todoItem)
            updateDoneAmount()
            // TODO: save changes locally
        }
    }
}
