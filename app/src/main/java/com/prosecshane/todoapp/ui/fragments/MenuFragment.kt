package com.prosecshane.todoapp.ui.fragments
//
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.prosecshane.todoapp.R
//import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
//import com.prosecshane.todoapp.ui.view.PreviewAdapter
//import com.prosecshane.todoapp.ui.view.RecyclerOffsetItemDecoration
//import com.prosecshane.todoapp.util.toPx
//
//// Фрагмент меню - главный фрагмент
//class MenuFragment : Fragment(R.layout.fragment_list) {
//    private val todoItemsViewModel: TodoItemsViewModel by activityViewModels()
//    private lateinit var previewRecyclerView: RecyclerView
//    private lateinit var previewAdapter: PreviewAdapter
//
//    // При создании
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val navController = findNavController()
//
//        // Готовим RecyclerView
//        previewRecyclerView = view.findViewById(R.id.todo_preview_list)
//        previewAdapter = PreviewAdapter(
//            { pos: Int ->
//                // При нажатии на чекбокс отмечаем/переотмечаем одно из дел и обновляем
//                todoItemsViewModel.todoItemsRepository.value!!.items[pos].done =
//                    !todoItemsViewModel.todoItemsRepository.value!!.items[pos].done
//                updateRecyclerView()
//            },
//            { pos: Int ->
//                // При нажатии на текст запоминаем данные и переходим в другой фрагмент
//                todoItemsViewModel.currentTodoItem.value =
//                    todoItemsViewModel.todoItemsRepository.value!!.items[pos].id
//                todoItemsViewModel.currentTodoPos.value = pos
//                navController.navigate(R.id.action_frag_menu_to_frag_editor)
//            }
//        )
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        previewRecyclerView.adapter = previewAdapter
//        previewRecyclerView.layoutManager = layoutManager
//
//        previewRecyclerView.addItemDecoration(
//            RecyclerOffsetItemDecoration(bottomOffset = 8f.toPx.toInt())
//        )
//        val refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.todo_preview_refresh)
//        refreshLayout.setOnRefreshListener {
//            updateRecyclerView()
//            refreshLayout.isRefreshing = false
//        }
//
//        // Обновление счетика выполненных дел
//        todoItemsViewModel.todoItemsRepository.observe(viewLifecycleOwner) { repo ->
//            view.findViewById<TextView>(
//                R.id.main_done_amount
//            ).text = getString(R.string.done_amount_var, repo.items.filter{ it.done }.size)
//        }
//
//        // Показать/скрыть выполненное
//        val showButton = view.findViewById<ImageButton>(R.id.main_show_done)
//        showButton.setImageResource(
//            if (todoItemsViewModel.onlyUndone.value == true) R.drawable.ic_visible_off else R.drawable.ic_visible
//        )
//        showButton.setOnClickListener {
//            val onlyUndone = todoItemsViewModel.onlyUndone
//            when (onlyUndone.value) {
//                false -> {
//                    onlyUndone.value = true
//                    updateRecyclerView()
//                    showButton.setImageResource(R.drawable.ic_visible_off)
//                }
//                true -> {
//                    onlyUndone.value = false
//                    updateRecyclerView()
//                    showButton.setImageResource(R.drawable.ic_visible)
//                }
//                else -> {}
//            }
//        }
//
//        // Добавить дело через кнопку снизу
//        val addButton = view.findViewById<FloatingActionButton>(R.id.add_item)
//        addButton.setOnClickListener {
//            todoItemsViewModel.currentTodoItem.value = "${(0..100000).random()}"
//            todoItemsViewModel.currentTodoPos.value = -1
//            navController.navigate(R.id.action_frag_menu_to_frag_editor)
//        }
//    }
//
//    // Обновление списка через адаптер
//    private fun updateRecyclerView() {
//        previewAdapter.items = todoItemsViewModel.todoItemsRepository.value?.getItems(
//            todoItemsViewModel.onlyUndone.value == true
//        ) ?: listOf()
//    }
//
//    // Обновляем при возвращении из другого фрагмента
//    override fun onResume() {
//        super.onResume()
//        updateRecyclerView()
//    }
//}