package com.prosecshane.todoapp.data

import java.util.Date

// Дата-класс для самого дела, поля очевидны
data class TodoItem(
    var id: String,
    var text: String,
    var importance: Importance,
    var deadline: Date? = null,
    var done: Boolean,
    var createdOn: Date,
    var editedOn: Date? = null,
)
