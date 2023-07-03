package com.prosecshane.todoapp.ui.stateholders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class TodoItemViewModel : ViewModel() {
    fun deleteItem(todoItem: TodoItem){
        viewModelScope.launch(Dispatchers.IO) {
            TodoItemsRepository.deleteItem(todoItem)
        }
    }

    fun saveItem(todoItem: TodoItem, isEdited:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            if(isEdited){
                TodoItemsRepository.updateItem(todoItem)
            }else{
                TodoItemsRepository.addTask(todoItem)
            }

        }
    }
}