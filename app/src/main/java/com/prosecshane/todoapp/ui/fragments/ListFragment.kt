package com.prosecshane.todoapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ioc.ApplicationComponent
import com.prosecshane.todoapp.ioc.ListFragmentComponent
import com.prosecshane.todoapp.ioc.TodoItemsPreviewComponent
import com.prosecshane.todoapp.ui.App
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
import com.prosecshane.todoapp.util.getDeviceId

// ListFragment - first fragment, contains the list of all items
@RequiresApi(Build.VERSION_CODES.M)
class ListFragment : Fragment() {
    private val applicationComponent = App.applicationComponent()
    private lateinit var fragmentComponent: ListFragmentComponent
    private var previewComponent: TodoItemsPreviewComponent? = null
    private val viewModel: TodoItemsViewModel by activityViewModels { applicationComponent.viewModelFactory }

    // use navController to get to another Fragment
    private fun navigateTo(id: Int) {
        val navController = findNavController()
        navController.navigate(id)
    }

    // setup Fragment Component
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ListFragmentComponent(
            applicationComponent,
            this,
            viewModel
        )
    }

    // Get rootView, setup Recycler View and other Views
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        previewComponent = TodoItemsPreviewComponent(
            fragmentComponent,
            view,
            viewLifecycleOwner,
        ).apply { todoItemsPreviewController.setUpViews() }

        bindVisibilityButton(view.findViewById(R.id.main_show_done))
        bindDoneAmount(view.findViewById(R.id.main_done_amount))
        bindAddTodoItemButton(view.findViewById(R.id.add_item))
        return view
    }

    // Change icon depending on the visibility of done tasks
    private fun styleVisibilityButton(button: ImageButton, onlyUndone: Boolean) {
        button.setImageResource(
            if (onlyUndone) R.drawable.ic_visible_off else R.drawable.ic_visible
        )
    }

    // Setup the visibility button
    private fun bindVisibilityButton(button: ImageButton) {
        styleVisibilityButton(button, viewModel.onlyUndone.value as Boolean)
        button.setOnClickListener {
            viewModel.switchVisible()
            styleVisibilityButton(button, viewModel.onlyUndone.value as Boolean)
        }
    }

    // Setup the "Done" statistic
    private fun bindDoneAmount(view: TextView) {
        viewModel.doneAmount.observe(viewLifecycleOwner) { newAmount ->
            view.text = getString(R.string.done_amount_var, newAmount)
        }
    }

    // Setup the button to add a new item
    private fun bindAddTodoItemButton(button: FloatingActionButton) {
        button.setOnClickListener {
            val newTodoItem = TodoItem(id = "NEW-ITEM", deviceId = getDeviceId(requireContext()))
            viewModel.setCurrentTodoItem(newTodoItem)
            navigateTo(R.id.action_fraglist_to_fragitem)
        }
    }

    // Destroy the RecyclerView component on destroy to avoid memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        previewComponent = null
    }
}