package com.prosecshane.todoapp.data.database.response

import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    var item: TodoItem,
    var status: String? = null,
    var revision: Int? = null
)
