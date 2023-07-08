package com.prosecshane.todoapp.ui.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ioc.TodoItemFragmentComponent
import com.prosecshane.todoapp.ui.activities.MainActivity
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
import com.prosecshane.todoapp.util.getDeviceId
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

// Fragment, that edits an item
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemFragment : Fragment() {
    private lateinit var todoItemFragmentComponent: TodoItemFragmentComponent
    lateinit var dateFormat: SimpleDateFormat
    lateinit var calendar: Calendar
    lateinit var viewModel: TodoItemsViewModel

    private lateinit var rootView: View

    // Get the item that we work with through ViewModel
    private fun getCurrentTodoItem(): TodoItem {
        return viewModel.currentTodoItem.value as TodoItem
    }

    // Navigate to the previous Fragment
    private fun navigateTo(id: Int) {
        val navController = findNavController()
        navController.navigate(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dependency Injection
        todoItemFragmentComponent = (activity as MainActivity)
            .mainActivityComponent
            .todoItemFragmentComponent()
        todoItemFragmentComponent.inject(this)
    }

    // Setup the Fragment and get the rootView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_item, container, false)
        return rootView
    }

    // Update/Setup the Fragment on resume
    override fun onResume() {
        super.onResume()
        setupButtons()
        setupInteractiveViews()
    }

    // Setup the Exit Button
    private fun setupExitButton(button: ImageButton) {
        button.setOnClickListener {
            navigateTo(R.id.action_fragitem_to_fraglist)
        }
    }

    // Setup the Delete Button
    private fun setupDeleteButton(button: MaterialButton) {
        button.setOnClickListener {
            viewModel.onTodoItemDeleted(getCurrentTodoItem())
            navigateTo(R.id.action_fragitem_to_fraglist)
        }
    }

    // Setup the Save Button
    private fun setupSaveButton(button: MaterialButton) {
        button.setOnClickListener {
            // Collect all data
            val currentTodoItem = getCurrentTodoItem()
            val isNewTodoItem = (currentTodoItem.id == "NEW-ITEM")

            val doneValue = currentTodoItem.done
            val textValue = rootView.findViewById<EditText>(R.id.editor_text).text.toString()
            val importanceValue = when (rootView.findViewById<RadioGroup>(
                R.id.editor_importance
            ).checkedRadioButtonId) {
                R.id.editor_high -> Importance.HIGH
                R.id.editor_mid -> Importance.MID
                R.id.editor_low -> Importance.LOW
                -1 -> Importance.MID
                else -> throw IllegalArgumentException(
                    "checkedId of the RadioGroup doesn't match any values"
                )
            }
            val deadlineValue = currentTodoItem.deadline
            val createdOnValue = currentTodoItem.createdOn
            val editedOnValue = System.currentTimeMillis()

            // Create and store the edited/new item
            var changedTodoItem = TodoItem(
                done = doneValue,
                text = textValue,
                importance = importanceValue,
                deadline = deadlineValue,
                createdOn = createdOnValue,
                editedOn = editedOnValue,
                deviceId = getDeviceId(requireContext()),
            )
            if (isNewTodoItem) {
                viewModel.onTodoItemAdded(changedTodoItem)
            } else {
                changedTodoItem = changedTodoItem.copy(id = currentTodoItem.id)
                viewModel.onTodoItemChanged(changedTodoItem)
            }
            navigateTo(R.id.action_fragitem_to_fraglist)
        }
    }

    // --- Setup all Buttons (look higher) ---
    private fun setupButtons() {
        setupExitButton(rootView.findViewById(R.id.editor_exit))
        setupDeleteButton(rootView.findViewById(R.id.editor_delete))
        setupSaveButton(rootView.findViewById(R.id.editor_save))
    }

    // Setup the Text Editor
    private fun setupEditText(view: EditText, textValue: String) {
        view.setText(textValue)
    }

    // Setup the Importance picker
    private fun setupRadioGroup(group: RadioGroup, importance: Importance) {
        group.check(when (importance) {
            Importance.LOW  -> R.id.editor_low
            Importance.MID  -> R.id.editor_mid
            Importance.HIGH -> R.id.editor_high
        })
    }

    // Setup the Deadline picker
    private fun setupDeadline(
        clickable: LinearLayout,
        deadlineView: TextView,
        checkBox: CheckBox,
        deadlineValue: Long?
    ) {
        if (deadlineValue == null) {
            deadlineView.text = getString(R.string.preview_date_placeholder)
            checkBox.isChecked = false
        } else {
            deadlineView.text = dateFormat.format(Date(deadlineValue))
            checkBox.isChecked = true
        }

        checkBox.setOnClickListener {
            val currentTodoItem = getCurrentTodoItem()
            if (currentTodoItem.deadline == null) {
                checkBox.isChecked = false
                calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, newYear, newMonth, newDay ->
                        calendar.set(newYear, newMonth, newDay, 0, 0)
                        viewModel.setCurrentTodoItem(
                            currentTodoItem.copy(deadline = calendar.time.time)
                        )
                        checkBox.isChecked = true
                        deadlineView.text = dateFormat.format(calendar.time)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            } else {
                checkBox.isChecked = false
                deadlineView.text = getString(R.string.preview_date_placeholder)
                viewModel.setCurrentTodoItem(currentTodoItem.copy(deadline = null))
            }
        }

        clickable.setOnClickListener {
            val currentTodoItem = getCurrentTodoItem()
            if (currentTodoItem.deadline != null) {
                calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, newYear, newMonth, newDay ->
                        calendar.set(newYear, newMonth, newDay, 0, 0)
                        viewModel.setCurrentTodoItem(
                            currentTodoItem.copy(deadline = calendar.time.time)
                        )
                        deadlineView.text = dateFormat.format(calendar.time)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }
        }
    }

    // --- Setup the Interactive parts of the Fragment (all of the above before buttons) ---
    private fun setupInteractiveViews() {
        val currentTodoItem = viewModel.currentTodoItem.value as TodoItem
        setupEditText(
            rootView.findViewById(R.id.editor_text),
            currentTodoItem.text
        )
        setupRadioGroup(
            rootView.findViewById(R.id.editor_importance),
            currentTodoItem.importance
        )
        setupDeadline(
            rootView.findViewById(R.id.editor_date),
            rootView.findViewById(R.id.editor_date_view),
            rootView.findViewById(R.id.editor_has_date),
            currentTodoItem.deadline
        )
    }
}
