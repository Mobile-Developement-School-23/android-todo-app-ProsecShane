package com.prosecshane.todoapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.Importance
import com.prosecshane.todoapp.data.TodoItem
import com.prosecshane.todoapp.model.ItemsViewModel
import com.prosecshane.todoapp.model.PreviewAdapter
import com.prosecshane.todoapp.model.PreviewOffsetItemDecoration
import com.prosecshane.todoapp.util.toPx
import java.text.SimpleDateFormat
import java.util.*

// Фрагмент меню - главный фрагмент
class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val itemsViewModel: ItemsViewModel by activityViewModels()
    private lateinit var previewRecyclerView: RecyclerView
    private lateinit var previewAdapter: PreviewAdapter

    // При создании
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        // Готовим RecyclerView
        previewRecyclerView = view.findViewById(R.id.todo_preview_list)
        previewAdapter = PreviewAdapter(
            { pos: Int ->
                // При нажатии на чекбокс отмечаем/переотмечаем одно из дел и обновляем
                itemsViewModel.todoItemsRepository.value!!.items[pos].done =
                    !itemsViewModel.todoItemsRepository.value!!.items[pos].done
                updateRecyclerView()
            },
            { pos: Int ->
                // При нажатии на текст запоминаем данные и переходим в другой фрагмент
                itemsViewModel.currentTodoItem.value =
                    itemsViewModel.todoItemsRepository.value!!.items[pos].id
                itemsViewModel.currentTodoPos.value = pos
                navController.navigate(R.id.action_frag_menu_to_frag_editor)
            }
        )
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        previewRecyclerView.adapter = previewAdapter
        previewRecyclerView.layoutManager = layoutManager

        previewRecyclerView.addItemDecoration(
            PreviewOffsetItemDecoration(bottomOffset = 8f.toPx.toInt())
        )
        val refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.todo_preview_refresh)
        refreshLayout.setOnRefreshListener {
            updateRecyclerView()
            refreshLayout.isRefreshing = false
        }

        // Обновление счетика выполненных дел
        itemsViewModel.todoItemsRepository.observe(viewLifecycleOwner) { repo ->
            view.findViewById<TextView>(
                R.id.main_done_amount
            ).text = getString(R.string.done_amount_var, repo.items.filter{ it.done }.size)
        }

        // Показать/скрыть выполненное
        val showButton = view.findViewById<ImageButton>(R.id.main_show_done)
        showButton.setImageResource(
            if (itemsViewModel.onlyUndone.value == true) R.drawable.ic_visible_off else R.drawable.ic_visible
        )
        showButton.setOnClickListener {
            val onlyUndone = itemsViewModel.onlyUndone
            when (onlyUndone.value) {
                false -> {
                    onlyUndone.value = true
                    updateRecyclerView()
                    showButton.setImageResource(R.drawable.ic_visible_off)
                }
                true -> {
                    onlyUndone.value = false
                    updateRecyclerView()
                    showButton.setImageResource(R.drawable.ic_visible)
                }
                else -> {}
            }
        }

        // Добавить дело через кнопку снизу
        val addButton = view.findViewById<FloatingActionButton>(R.id.add_item)
        addButton.setOnClickListener {
            itemsViewModel.currentTodoItem.value = "${(0..100000).random()}"
            itemsViewModel.currentTodoPos.value = -1
            navController.navigate(R.id.action_frag_menu_to_frag_editor)
        }
    }

    // Обновление списка через адаптер
    private fun updateRecyclerView() {
        previewAdapter.items = itemsViewModel.todoItemsRepository.value?.getItems(
            itemsViewModel.onlyUndone.value == true
        ) ?: listOf()
    }

    // Обновляем при возвращении из другого фрагмента
    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }
}