package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.prosecshane.todoapp.ui.fragments.ListFragment
import com.prosecshane.todoapp.ui.view.TodoItemsDiffCalculator
import com.prosecshane.todoapp.ui.view.TodoItemsListAdapter
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel

@RequiresApi(Build.VERSION_CODES.M)
class ListFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: ListFragment,
    val viewModel: TodoItemsViewModel
) {
    val adapter = TodoItemsListAdapter(
        viewModel,
        TodoItemsDiffCalculator(),
        fragment.findNavController()
    )
}