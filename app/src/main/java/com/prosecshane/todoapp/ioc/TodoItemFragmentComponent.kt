package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.ui.fragments.TodoItemFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.M)
class TodoItemFragmentComponent(
    private val activityComponent: MainActivityComponent
) {
    private val viewModel = activityComponent.viewModel

    fun inject(instance: TodoItemFragment) {
        instance.dateFormat = SimpleDateFormat("d MMM yyyy", Locale.US)
        instance.calendar = Calendar.getInstance()
        instance.viewModel = viewModel
    }
}
