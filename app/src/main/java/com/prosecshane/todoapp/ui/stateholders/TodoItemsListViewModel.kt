package com.prosecshane.todoapp.ui.stateholders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class TodoItemsListViewModel : ViewModel() {
    val tasks = MutableLiveData<List<TodoItem>>()
    val tasksUncompleted = MutableLiveData<List<TodoItem>>()
    val completedTasksCount = MutableLiveData<Int>()

    val serverCodes = MutableLiveData<Int>()
    var serverCodesJob: Job? = null
    var syncWithServerJob: Job? = null

    var tasksJob: Job? = null
    var uncompletedTasksJob: Job? = null
    var completeTasksCountJob: Job? = null

    fun subscribeOnTasksChanges() {
        tasksJob?.cancelIfInUse()
        tasksJob = viewModelScope.launch {
            TodoItemsRepository.database.todoItemDBMS().getAll().collect {
                tasks.value = it
            }
        }
    }

    fun unsubscribeOnTasksChanges() {
        tasksJob?.cancel()
    }

    fun subscribeOnUncompletedTasksChanges() {
        uncompletedTasksJob?.cancelIfInUse()
        uncompletedTasksJob = viewModelScope.launch {
            TodoItemsRepository.database.todoItemDBMS().getAllUncompleted().collect {
                tasksUncompleted.value = it
            }
        }
    }

    fun unsubscribeOnUncompletedTasksChanges() {
        uncompletedTasksJob?.cancel()
    }

    fun subscribeOnCompletedTasksCount() {
        completeTasksCountJob?.cancelIfInUse()
        completeTasksCountJob = viewModelScope.launch {
            TodoItemsRepository.database.todoItemDBMS().getNumOfCompleted().collect {
                completedTasksCount.value = it
            }
        }
    }

    fun unsubscribeOnCompletedTasksCount() {
        completeTasksCountJob?.cancel()
    }

    fun subscribeOnServerCodes(){
        serverCodesJob?.cancelIfInUse()
        serverCodesJob = viewModelScope.launch {
            TodoItemsRepository.code.collect{
                serverCodes.value = it
            }
        }
    }

    fun unsubscribeOnServerCodes(){
        serverCodesJob?.cancel()
    }
    fun syncWithServer(){
        syncWithServerJob?.cancelIfInUse()
        syncWithServerJob=viewModelScope.launch(Dispatchers.IO) {
            TodoItemsRepository.sync()
        }
    }

    private fun Job.cancelIfInUse() {
        if (!isCompleted)
            cancel()
    }
}
