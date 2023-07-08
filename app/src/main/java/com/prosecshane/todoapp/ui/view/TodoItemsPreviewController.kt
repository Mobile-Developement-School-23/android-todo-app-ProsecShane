package com.prosecshane.todoapp.ui.view

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
import com.prosecshane.todoapp.util.toPx

// RecyclerView controller
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemsPreviewController(
    private val activity: Activity,
    rootView: View,
    private val adapter: TodoItemsListAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoItemsViewModel
) {
    private val recyclerView: RecyclerView = rootView.findViewById(R.id.todo_preview_list)
    private val swipeRefreshLayout: SwipeRefreshLayout =
        rootView.findViewById(R.id.todo_preview_refresh)

    // Setup everything below
    fun setUpViews() {
        setUpTodoItemsList()
        setUpSwipeToRefresh()
    }

    // Setup the RecyclerView
    private fun setUpTodoItemsList() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            RecyclerOffsetItemDecoration(bottomOffset = 8f.toPx.toInt())
        )
        viewModel.todoItems.observe(lifecycleOwner) { newTodoItems ->
            adapter.submitList(
                if (viewModel.onlyUndone.value as Boolean) newTodoItems.filter{!it.done}
                else newTodoItems
            )
            viewModel.updateDoneAmount(newTodoItems)
            swipeRefreshLayout.isRefreshing = false
        }
        viewModel.onlyUndone.observe(lifecycleOwner) { newValue ->
            if (newValue) adapter.submitList(viewModel.todoItems.value.orEmpty().filter{!it.done})
            else adapter.submitList(viewModel.todoItems.value.orEmpty())
        }
    }

    // Setup the refresher
    private fun setUpSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.updateTodoItems()
        }
    }
}
