package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ui.fragments.TodoItemFragment
import com.prosecshane.todoapp.util.AttrColorGetter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Component that follows the TodoItem Fragment
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemFragmentComponent(
    private val activityComponent: MainActivityComponent
) {
    private val viewModel = activityComponent.viewModel

    fun inject(instance: TodoItemFragment) {
        instance.viewModel = viewModel
        instance.dateFormat = SimpleDateFormat("d MMM yyyy", Locale.US)
        instance.calendar = Calendar.getInstance()
    }
}
