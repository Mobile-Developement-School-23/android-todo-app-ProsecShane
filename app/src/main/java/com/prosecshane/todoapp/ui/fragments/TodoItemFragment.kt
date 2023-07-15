package com.prosecshane.todoapp.ui.fragments

import android.app.AlertDialog
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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ioc.TodoItemFragmentComponent
import com.prosecshane.todoapp.ui.activities.MainActivity
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
import com.prosecshane.todoapp.util.AttrColorGetter
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
    private lateinit var attrColorGetter: AttrColorGetter

    private lateinit var confirmExitDialog: AlertDialog
    private lateinit var confirmDeleteDialog: AlertDialog
    private lateinit var importanceDialog: BottomSheetDialog
    private lateinit var changeDeadlineDialog: AlertDialog
    private lateinit var datePickerDialog: DatePickerDialog

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

        // Setup Transition
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    // Setup the Fragment and get the rootView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_item, container, false)

        // Setup the Attribute Color Getter and Dialogs
        attrColorGetter = AttrColorGetter(requireContext())
        setupConfirmExitDialog()
        setupConfirmDeleteDialog()
        setupImportanceDialog()
        setupChangeDeadlineDialogs()

        return rootView
    }

    // Setup the Exit Confirmation Dialog
    private fun setupConfirmExitDialog() {
        confirmExitDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_exit_title))
            .setMessage(getString(R.string.confirm_exit_text))
            .setPositiveButton(R.string.confirm_exit_yes) { _, _ ->
                navigateTo(R.id.action_fragitem_to_fraglist)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    // Setup the Delete Confirmation Dialog
    private fun setupConfirmDeleteDialog() {
        confirmDeleteDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_text))
            .setPositiveButton(R.string.confirm_delete_yes) { _, _ ->
                viewModel.onTodoItemDeleted(getCurrentTodoItem())
                navigateTo(R.id.action_fragitem_to_fraglist)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    // Setup the Bottom Sheet Importance Dialog
    private fun setupImportanceDialog() {
        importanceDialog = BottomSheetDialog(requireContext())
        importanceDialog.setContentView(R.layout.importance_bottom_sheet)
        val high = importanceDialog.findViewById<MaterialButton>(R.id.editor_high) as MaterialButton
        val mid = importanceDialog.findViewById<MaterialButton>(R.id.editor_mid) as MaterialButton
        val low = importanceDialog.findViewById<MaterialButton>(R.id.editor_low) as MaterialButton
        high.setOnClickListener {
            viewModel.setCurrentTodoItem(getCurrentTodoItem().copy(importance = Importance.HIGH))
            styleImportanceButton()
            importanceDialog.hide()
        }
        mid.setOnClickListener {
            viewModel.setCurrentTodoItem(getCurrentTodoItem().copy(importance = Importance.MID))
            styleImportanceButton()
            importanceDialog.hide()
        }
        low.setOnClickListener {
            viewModel.setCurrentTodoItem(getCurrentTodoItem().copy(importance = Importance.LOW))
            styleImportanceButton()
            importanceDialog.hide()
        }
    }

    // Setup Deadline Dialogs (Date Picker and Alert)
    private fun setupChangeDeadlineDialogs() {
        calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                calendar.set(y, m , d, 0, 0)
                viewModel.setCurrentTodoItem(
                    getCurrentTodoItem().copy(deadline = calendar.time.time)
                )
                styleDeadlineButton()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        changeDeadlineDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.change_deadline_title))
            .setMessage(getString(R.string.change_deadline_text))
            .setPositiveButton(R.string.change_deadline_yes) { _, _ ->
                datePickerDialog.show()
            }
            .setNegativeButton(R.string.change_deadline_no) { _, _ ->
                viewModel.setCurrentTodoItem(getCurrentTodoItem().copy(deadline = null))
                styleDeadlineButton()
            }
            .setNeutralButton(R.string.cancel, null)
            .create()
    }

    // Update/Setup the Fragment on resume
    override fun onResume() {
        super.onResume()
        setupExitButton()
        setupDeleteButton()
        setupSaveButton()
        setupImportanceButton()
        setupDeadlineButton()
        setupText()
    }

    // Setup the Exit Button
    private fun setupExitButton() {
        val button = rootView.findViewById<ImageButton>(R.id.editor_exit)
        button.setOnClickListener { confirmExitDialog.show() }
    }

    // Setup the Delete Button
    private fun setupDeleteButton() {
        val button = rootView.findViewById<ImageButton>(R.id.editor_delete)
        button.setOnClickListener { confirmDeleteDialog.show() }
    }

    // Setup the Save Button
    private fun setupSaveButton() {
        val button = rootView.findViewById<ImageButton>(R.id.editor_save)
        button.setOnClickListener {
            // Collect all data
            val currentTodoItem = getCurrentTodoItem()
            val isNewTodoItem = (currentTodoItem.id == "NEW-ITEM")

            val doneValue = currentTodoItem.done
            val textValue = rootView.findViewById<EditText>(R.id.editor_text).text.toString()
            val importanceValue = currentTodoItem.importance
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

    // Setup the Importance Button
    private fun setupImportanceButton() {
        styleImportanceButton()
        val button = rootView.findViewById<MaterialButton>(R.id.editor_importance)
        button.setOnClickListener {
            importanceDialog.show()
        }
    }

    // Style the Importance Button
    private fun styleImportanceButton() {
        val button = rootView.findViewById<MaterialButton>(R.id.editor_importance)
        button.text = when (getCurrentTodoItem().importance) {
            Importance.LOW  -> getString(R.string.low_importance)
            Importance.MID  -> getString(R.string.mid_importance)
            Importance.HIGH -> getString(R.string.high_importance)
        }
        button.setTextColor(
            if (getCurrentTodoItem().importance == Importance.HIGH) attrColorGetter.get(
                com.google.android.material.R.attr.colorError
            ) else attrColorGetter.get(com.google.android.material.R.attr.colorOnBackground)
        )
    }

    // Setup the Deadline Button
    private fun setupDeadlineButton() {
        styleDeadlineButton()
        val button = rootView.findViewById<MaterialButton>(R.id.editor_deadline)
        button.setOnClickListener {
            if (getCurrentTodoItem().deadline == null) datePickerDialog.show()
            else changeDeadlineDialog.show()
        }
    }

    // Style the Deadline Button
    private fun styleDeadlineButton() {
        val deadline = getCurrentTodoItem().deadline
        val button = rootView.findViewById<MaterialButton>(R.id.editor_deadline)
        button.text = (
                if (deadline == null)
                    getString(R.string.preview_date_placeholder)
                else dateFormat.format(Date(deadline))
                )
    }

    // Setup the EditText Field
    private fun setupText() {
        val text = rootView.findViewById<EditText>(R.id.editor_text)
        text.setText(getCurrentTodoItem().text)
    }
}
