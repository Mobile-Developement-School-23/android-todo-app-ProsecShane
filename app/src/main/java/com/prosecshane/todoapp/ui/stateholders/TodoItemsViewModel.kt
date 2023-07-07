package com.prosecshane.todoapp.ui.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import com.prosecshane.todoapp.data.util.SharedPreferencesConstants.ONLY_UNDONE_KEY
import com.prosecshane.todoapp.data.util.SharedPreferencesUtil
import com.prosecshane.todoapp.ui.App
import kotlinx.coroutines.launch

// View Model, contains relevant information
class TodoItemsViewModel(
    private val todoItemsRepository: TodoItemsRepository,
) : ViewModel() {
    private val sharedPrefs = SharedPreferencesUtil(App.getApplicationContext())

    // Show only undone or show all
    private val _onlyUndone = MutableLiveData(
        sharedPrefs.get(ONLY_UNDONE_KEY, false) as Boolean
    )
    val onlyUndone: LiveData<Boolean> = _onlyUndone

    // Currently working item (for TodoItemFragment)
    private val _currentTodoItem = MutableLiveData(TodoItem(
        id = "ERROR_ITEM", done = false,
        text = "If you see this, an error occurred. Report this bug to the developer",
        importance = Importance.HIGH, deadline = null, deviceId = "ERROR",
    ))
    val currentTodoItem: LiveData<TodoItem> = _currentTodoItem

    // LiveData from the repository
    val todoItems: LiveData<List<TodoItem>> = todoItemsRepository.todoItems

    // How much is done (for the ListFragment)
    private val _doneAmount = MutableLiveData(0)
    val doneAmount: LiveData<Int> = _doneAmount

    // On start: update items and amount of done
    init { updateTodoItems() }

    // Update the LiveData with amount
    fun updateDoneAmount(todoItemsList: List<TodoItem> = todoItems.value.orEmpty()) {
        _doneAmount.value = todoItemsList.filter { it.done }.size
    }

    // Flip the switch of visibility
    fun switchVisible() {
        viewModelScope.launch {
            val visibility = onlyUndone.value as Boolean
            _onlyUndone.value = !visibility
            sharedPrefs.set(ONLY_UNDONE_KEY, !visibility)
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
            todoItemsRepository.deleteTodoItem(todoItem)
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
