package com.prosecshane.todoapp.data.model

import java.util.UUID

// TODO: Comment all code

// Data class for a single item
data class TodoItem(
    val id:             String = UUID.randomUUID().toString(),
    val done:           Boolean = false,
    val text:           String = "",
    val importance:     Importance = Importance.MID,        // TODO: convert enum to String
    val deadline:       Long? = null,                       // TODO: convert to null correctly
    val createdOn:      Long = System.currentTimeMillis(),
    val editedOn:       Long = createdOn,
)
