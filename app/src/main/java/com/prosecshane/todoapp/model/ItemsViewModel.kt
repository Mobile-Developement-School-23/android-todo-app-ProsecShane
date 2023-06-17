package com.prosecshane.todoapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prosecshane.todoapp.data.TodoItemsRepository

// ВьюМодел, содержит репозиторий, ID и индекс редактируемого дела и показываем или нет выполненное
class ItemsViewModel : ViewModel() {
    val todoItemsRepository = MutableLiveData(TodoItemsRepository())
    val currentTodoItem = MutableLiveData("_ERROR")
    val currentTodoPos = MutableLiveData(-1)
    val onlyUndone = MutableLiveData(false)
}
