package com.prosecshane.todoapp.data.database.response

import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.serialization.Serializable

@Serializable
data class ItemListResponse(
    var itemList: List<TodoItem>,
    var status: String? = null,
    var revision: Int? = null
)
