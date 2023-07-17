package com.prosecshane.todoapp.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
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
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemFragment : Fragment() {
    // TODO: Component edited in Compose
    private lateinit var todoItemFragmentComponent: TodoItemFragmentComponent
    lateinit var viewModel: TodoItemsViewModel
    lateinit var dateFormat: SimpleDateFormat
    lateinit var calendar: Calendar

    // Navigate to the previous Fragment
    private fun navigateBack() {
        findNavController().navigate(R.id.action_fragitem_to_fraglist)
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
    ): View {
        return ComposeView(requireContext()).apply {
            setContent { EditFragment() }
        }
    }

    @Composable
    fun EditFragment() {
        val todoItem: MutableState<TodoItem> =
            remember { mutableStateOf(viewModel.currentTodoItem.value as TodoItem) }
        val inBottomSheet: MutableState<Boolean> = remember { mutableStateOf(false) }

        Column(
            Modifier
                .background(Color.White)
                .fillMaxHeight()
        ) {
            EditNavBar(todoItem)
            EditButtonsRow(todoItem, inBottomSheet)

            TextField(
                value = todoItem.value.text,
                onValueChange = { newText -> todoItem.value = todoItem.value.copy(text = newText) },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(100f)
                    .fillMaxWidth(),
                placeholder = { Text(getString(R.string.editor_hint)) }
            )

            if (inBottomSheet.value) { ImportanceSheet(
                todoItem, inBottomSheet
            ) }
        }
    }

    @Composable
    fun ImportanceSheet(
        todoItem: MutableState<TodoItem>,
        inBottomSheet: MutableState<Boolean>,
    ) {
        ModalBottomSheet(onDismissRequest = {
            inBottomSheet.value = false
        }) {
            Column(Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        todoItem.value = todoItem.value.copy(importance = Importance.HIGH)
                        inBottomSheet.value = false
                    }
                ) { Text(getString(R.string.high_importance)) }
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        todoItem.value = todoItem.value.copy(importance = Importance.MID)
                        inBottomSheet.value = false
                    }
                ) { Text(getString(R.string.mid_importance)) }
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        todoItem.value = todoItem.value.copy(importance = Importance.LOW)
                        inBottomSheet.value = false
                    }
                ) { Text(getString(R.string.low_importance)) }
                Spacer(Modifier.size(50.dp))
            }
        }
    }

    @Composable
    fun EditNavBar(
        todoItem: MutableState<TodoItem>
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(11.dp)
                .fillMaxWidth()
        ) {
            BackButton()
            Row {
                DeleteButton(todoItem)
                SaveButton(todoItem)
            }
        }
    }

    @Composable
    fun BackButton() {
        IconButton(
            onClick = {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm_exit_title))
                    .setMessage(getString(R.string.confirm_exit_text))
                    .setPositiveButton(R.string.confirm_exit_yes) { _, _ -> navigateBack() }
                    .setNegativeButton(R.string.cancel, null)
                    .create().show()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null
            )
        }
    }

    @Composable
    fun DeleteButton(
        todoItem: MutableState<TodoItem>
    ) {
        IconButton(
            onClick = {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm_delete_title))
                    .setMessage(getString(R.string.confirm_delete_text))
                    .setPositiveButton(R.string.confirm_delete_yes) { _, _ ->
                        viewModel.onTodoItemDeleted(todoItem.value)
                        navigateBack()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .create().show()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }
    }

    @Composable
    fun SaveButton(
        todoItem: MutableState<TodoItem>
    ) {
        IconButton(
            onClick = {
                val isNewTodoItem = (todoItem.value.id == "NEW-ITEM")

                // Create and store the edited/new item
                val changedTodoItem = TodoItem(
                    done = todoItem.value.done,
                    text = todoItem.value.text,
                    importance = todoItem.value.importance,
                    deadline = todoItem.value.deadline,
                    createdOn = todoItem.value.createdOn,
                    editedOn = System.currentTimeMillis(),
                    deviceId = getDeviceId(requireContext()),
                )
                if (isNewTodoItem) {
                    viewModel.onTodoItemAdded(changedTodoItem)
                } else {
                    viewModel.onTodoItemChanged(
                        changedTodoItem.copy(id = todoItem.value.id)
                    )
                }
                navigateBack()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null
            )
        }
    }

    @Composable
    fun EditButtonsRow(
        todoItem: MutableState<TodoItem>,
        inBottomSheet: MutableState<Boolean>,
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // Importance Button
            Button(
                modifier = Modifier.weight(1f),
                onClick = { inBottomSheet.value = true }
            ) {
                Text(when (todoItem.value.importance) {
                    Importance.LOW  -> getString(R.string.low_importance)
                    Importance.MID  -> getString(R.string.mid_importance)
                    Importance.HIGH -> getString(R.string.high_importance)
                })
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null
                )
            }

            Spacer(
                Modifier.size(16.dp)
            )

            // Deadline Button
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        { _, y, m, d ->
                            calendar.set(y, m , d, 0, 0)
                            todoItem.value = todoItem.value.copy(
                                deadline = calendar.time.time
                            )
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    if (todoItem.value.deadline == null) {
                        calendar = Calendar.getInstance()
                        datePickerDialog.show()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.change_deadline_title))
                            .setMessage(getString(R.string.change_deadline_text))
                            .setPositiveButton(R.string.change_deadline_yes) { _, _ ->
                                datePickerDialog.show()
                            }
                            .setNegativeButton(R.string.change_deadline_no) { _, _ ->
                                todoItem.value = todoItem.value.copy(deadline = null)
                            }
                            .setNeutralButton(R.string.cancel, null)
                            .create().show()
                    }
                }
            ) {
                Text(if (todoItem.value.deadline == null)
                    getString(R.string.preview_date_placeholder)
                else dateFormat.format(Date(todoItem.value.deadline!!)))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null
                )
            }
        }
    }

    // TODO: Gone in Compose

    // Setup the Bottom Sheet Importance Dialog
//    private fun setupImportanceDialog() {
//        importanceDialog = BottomSheetDialog(requireContext())
//        importanceDialog.setContentView(R.layout.importance_bottom_sheet)
//        val high = importanceDialog.findViewById<MaterialButton>(R.id.editor_high) as MaterialButton
//        val mid = importanceDialog.findViewById<MaterialButton>(R.id.editor_mid) as MaterialButton
//        val low = importanceDialog.findViewById<MaterialButton>(R.id.editor_low) as MaterialButton
//        high.setOnClickListener {
//            todoItem = todoItem.copy(importance = Importance.HIGH)
//            styleImportanceButton()
//            importanceDialog.hide()
//        }
//        mid.setOnClickListener {
//            todoItem = todoItem.copy(importance = Importance.MID)
//            styleImportanceButton()
//            importanceDialog.hide()
//        }
//        low.setOnClickListener {
//            todoItem = todoItem.copy(importance = Importance.LOW)
//            styleImportanceButton()
//            importanceDialog.hide()
//        }
//    }
}
