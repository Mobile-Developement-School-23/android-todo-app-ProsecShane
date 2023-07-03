package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.data.datasource.HardCodedDataResource
import com.prosecshane.todoapp.data.repository.TodoItemsRepository

// Application component - contains information to be used by the whole app
@RequiresApi(Build.VERSION_CODES.M)
class ApplicationComponent {
    private val dataSource = HardCodedDataResource()
    private val todoItemsRepository = TodoItemsRepository.apply { this.dataSource = dataSource }
    val viewModelFactory = ViewModelFactory(todoItemsRepository)
}
