package com.prosecshane.todoapp.ui.view

import androidx.recyclerview.widget.DiffUtil
import com.prosecshane.todoapp.data.model.TodoItem

class TodoItemsDiffCalculator : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}
