package com.prosecshane.todoapp.data.datasource

import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

// Hard-coded examples, simulates loading logic
class HardCodedDataSource : TodoItemsDataSource {
    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.US)
    private val currentTimeMillis = System.currentTimeMillis()
    private val todoItems = mutableListOf(
        TodoItem(
            id = "EXAMPLE-1",
            done = false,
            text = "Buy money",
            importance = Importance.MID,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-2",
            done = false,
            text = "Buy a lot of money. A whole bunch. Like, a lot of money!!!",
            importance = Importance.MID,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-3",
            done = false,
            text = "Buy so much money, that while I tell you about how much money I bought, there will be no space left in the description and you will only see ellipsis and will never get to now, how much money I bought.",
            importance = Importance.MID,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-4",
            done = false,
            text = "Pass the University Exams",
            importance = Importance.LOW,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-5",
            done = false,
            text = "Get groceries",
            importance = Importance.MID,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-6",
            done = false,
            text = "Hang out with friends",
            importance = Importance.HIGH,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-7",
            done = true,
            text = "Do homework",
            importance = Importance.MID,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-8",
            done = false,
            text = "Hand in homework",
            importance = Importance.MID,
            deadline = dateFormat.parse("1 jul 2023")?.time,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        ),
        TodoItem(
            id = "EXAMPLE-9",
            done = true,
            text = "Eat a hamburger",
            importance = Importance.MID,
            deadline = dateFormat.parse("9 oct 2021")?.time,
            createdOn = currentTimeMillis,
            deviceId = "DEVELOPER",
        )
    )

    override suspend fun loadTodoItems(): List<TodoItem> {
        delay(200L);
        return todoItems
    }
}
