package com.prosecshane.todoapp.ioc

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.prosecshane.todoapp.ui.fragments.ListFragment
import com.prosecshane.todoapp.ui.view.TodoItemsDiffCalculator
import com.prosecshane.todoapp.ui.view.TodoItemsListAdapter
import com.prosecshane.todoapp.ui.view.TodoItemsPreviewController

// Component that follows the List Fragment
@RequiresApi(Build.VERSION_CODES.M)
class ListFragmentComponent(
    private val activityComponent: MainActivityComponent
) {
    private val diffCalculator by lazy { TodoItemsDiffCalculator() }
    private val viewModel = activityComponent.viewModel

    fun inject(instance: ListFragment) {
        instance.viewModel = viewModel
        instance.previewController = TodoItemsPreviewController(
            instance.requireActivity(),
            instance.rootView,
            TodoItemsListAdapter(
                instance.viewModel,
                diffCalculator,
                instance.findNavController()
            ),
            instance.viewLifecycleOwner,
            instance.viewModel
        )
    }
}
