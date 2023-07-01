package com.prosecshane.todoapp.ioc

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import com.prosecshane.todoapp.ui.view.TodoItemsPreviewController

@RequiresApi(Build.VERSION_CODES.M)
class TodoItemsPreviewComponent(
    fragmentComponent: ListFragmentComponent,
    root: View,
    lifecycleOwner: LifecycleOwner
) {
    val todoItemsPreviewController = TodoItemsPreviewController(
        fragmentComponent.fragment.requireActivity(),
        root,
        fragmentComponent.adapter,
        lifecycleOwner,
        fragmentComponent.viewModel
    )
}
