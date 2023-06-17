package com.prosecshane.todoapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.Importance
import com.prosecshane.todoapp.data.TodoItem
import com.prosecshane.todoapp.model.ItemsViewModel
import java.text.SimpleDateFormat
import java.util.*

// Фрагмент для редактирования дела
class EditorFragment : Fragment(R.layout.fragment_editor) {
    // Вспомогательные поля
    private val itemsViewModel: ItemsViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var todoItem: TodoItem

    // Поля-элементы лэйаута
    private lateinit var exitButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var deleteButton: MaterialButton
    private lateinit var editText: EditText

    private lateinit var importance: RadioGroup
    private lateinit var highButton: RadioButton
    private lateinit var midButton: RadioButton
    private lateinit var lowButton: RadioButton

    private lateinit var dateSwitch: SwitchCompat
    private lateinit var dateSelect: LinearLayout
    private lateinit var dateView: TextView

    // При создании фрагмента...
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        // Определяем поля-элементы
        exitButton = view.findViewById(R.id.editor_exit)
        saveButton = view.findViewById(R.id.editor_save)
        deleteButton = view.findViewById(R.id.editor_delete)
        editText = view.findViewById(R.id.editor_text)

        importance = view.findViewById(R.id.editor_importance)
        highButton = view.findViewById(R.id.editor_high)
        midButton = view.findViewById(R.id.editor_mid)
        lowButton = view.findViewById(R.id.editor_low)

        dateSwitch = view.findViewById(R.id.editor_has_date)
        dateSelect = view.findViewById(R.id.editor_date)
        dateView = view.findViewById(R.id.editor_date_view)

        // Подгружаем данные из дела и устанавливаем OnClickListener-ы
        loadData()
        setOnClickListeners()
    }

    // Подгрузка данных из полученного дела
    private fun loadData() {
        // Либо получаем дело, либо создаем новое
        todoItem = if (itemsViewModel.currentTodoPos.value == -1) {
            TodoItem(
                id = itemsViewModel.currentTodoItem.value!!,
                text = "",
                importance = Importance.MID,
                done = false,
                createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003"),
            ) // TODO: actually save dates
        } else {
            itemsViewModel.todoItemsRepository.value!!
                .items[itemsViewModel.currentTodoPos.value!!]
        }

        // Вставляем данные в поля
        editText.setText(todoItem.text)

        importance.clearCheck()
        when (todoItem.importance) {
            Importance.HIGH -> highButton.isChecked = true
            Importance.MID -> midButton.isChecked = true
            Importance.LOW -> lowButton.isChecked = true
        }

        if (todoItem.deadline == null) {
            dateSwitch.isChecked = false
            dateView.text = getString(R.string.preview_date_placeholder)
        } else {
            dateSwitch.isChecked = true
            dateView.text = SimpleDateFormat("dd.MM.yyyy").format(todoItem.deadline)
        }
    }

    // Устанвливаемся onClickListener-ы
    private fun setOnClickListeners() {
        // Если выходим, то ничего не запоминаем
        exitButton.setOnClickListener {
            navController.navigate(R.id.action_frag_editor_to_frag_menu)
        }

        // Если сохраняем, то...
        saveButton.setOnClickListener {
            // Считываем текст дела
            todoItem.text = editText.text.toString()

            // Считываем важность
            if (highButton.isChecked) todoItem.importance = Importance.HIGH
            if (midButton.isChecked) todoItem.importance = Importance.MID
            if (lowButton.isChecked) todoItem.importance = Importance.LOW

            // Если это не новое дело...
            if (itemsViewModel.currentTodoPos.value != -1) {
                // Сохраняем данные во ВьюМодел
                itemsViewModel.todoItemsRepository.value!!.items[
                        itemsViewModel.currentTodoPos.value!!
                ] = todoItem
            } else {
                // Иначе добавляем новые данные во ВьюМодел
                itemsViewModel.todoItemsRepository.value!!.items.add(todoItem)
            }
            // Переходим к начальному фрагменту
            navController.navigate(R.id.action_frag_editor_to_frag_menu)
        }

        // При удалении
        deleteButton.setOnClickListener {
            // Если это не новое дело, то...
            if (itemsViewModel.currentTodoPos.value != -1) {
                // Удаляем его из ВьюМодел
                itemsViewModel.todoItemsRepository.value!!.items.removeAt(
                    itemsViewModel.currentTodoPos.value!!
                )
            }
            // Переходим к начальному фрагменту
            navController.navigate(R.id.action_frag_editor_to_frag_menu)
        }

        // Если нажали на переключатель дедлайна, то...
        dateSwitch.setOnClickListener {
            // Если он работал, то...
            if (todoItem.deadline != null) {
                // Очищаем дату
                dateSwitch.isChecked = false
                dateView.text = getString(R.string.preview_date_placeholder)
                todoItem.deadline = null
            } else {
                // Иначе считываем дату из диалога
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year_, month_, day_ ->
                        // Записываем в дело и в текст вью
                        val result = Calendar.getInstance()
                        result.set(year_, month_, day_, 0, 0)
                        todoItem.deadline = result.time
                        dateView.text = SimpleDateFormat("dd.MM.yyyy").format(todoItem.deadline)
                    },
                    year, month, day
                )
                datePickerDialog.show()
                // Если дату не выбрали, то отключаем переключатель
                if (todoItem.deadline == null) dateSwitch.isChecked = false
            }
        }

        // Если нажали на дату и она включена, то ...
        dateSelect.setOnClickListener {
            if (todoItem.deadline != null) {
                // Пытаемся ее считывать
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year_, month_, day_ ->
                        // Записываем в дело и в текст вью
                        val result = Calendar.getInstance()
                        result.set(year_, month_, day_, 0, 0)
                        todoItem.deadline = result.time
                        dateView.text = SimpleDateFormat("dd.MM.yyyy").format(todoItem.deadline)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }
        }
    }
}
