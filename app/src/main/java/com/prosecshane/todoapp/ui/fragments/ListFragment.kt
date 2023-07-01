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
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ioc.ApplicationComponent
import com.prosecshane.todoapp.ioc.ListFragmentComponent
import com.prosecshane.todoapp.ioc.TodoItemsPreviewComponent
import com.prosecshane.todoapp.ui.App
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel

@RequiresApi(Build.VERSION_CODES.M)
class ListFragment : Fragment() {
    private val applicationComponent = ApplicationComponent()
    private lateinit var fragmentComponent: ListFragmentComponent
    private var previewComponent: TodoItemsPreviewComponent? = null
    private val viewModel: TodoItemsViewModel by activityViewModels { applicationComponent.viewModelFactory }

    private fun navigateTo(id: Int) {
        val navController = findNavController()
        navController.navigate(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ListFragmentComponent(
            applicationComponent,
            this,
            viewModel
        )
    }

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

    private fun styleVisibilityButton(button: ImageButton, onlyUndone: Boolean) {
        button.setImageResource(
            if (onlyUndone) R.drawable.ic_visible_off else R.drawable.ic_visible
        )
    }

    private fun bindVisibilityButton(button: ImageButton) {
        styleVisibilityButton(button, viewModel.onlyUndone.value as Boolean)
        button.setOnClickListener {
            viewModel.switchVisible()
            styleVisibilityButton(button, viewModel.onlyUndone.value as Boolean)
        }
    }

    private fun bindDoneAmount(view: TextView) {
        viewModel.doneAmount.observe(viewLifecycleOwner) { newAmount ->
            view.text = getString(R.string.done_amount_var, newAmount)
        }
    }

    private fun bindAddTodoItemButton(button: FloatingActionButton) {
        button.setOnClickListener {
            val newTodoItem = TodoItem(id = "NEW-ITEM")
            viewModel.setCurrentTodoItem(newTodoItem)
            navigateTo(R.id.action_fraglist_to_fragitem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previewComponent = null
    }
}