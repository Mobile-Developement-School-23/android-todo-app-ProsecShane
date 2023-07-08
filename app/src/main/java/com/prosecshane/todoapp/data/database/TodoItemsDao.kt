package com.prosecshane.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prosecshane.todoapp.data.database.DatabaseConstants.TODO_ITEM_TABLE_NAME
import com.prosecshane.todoapp.data.model.TodoItem

// DAO for the Database
@Dao
interface TodoItemsDao {
    @Insert suspend fun addTodoItem(newTodoItem: TodoItem)
    @Update suspend fun changeTodoItem(changedTodoItem: TodoItem)
    @Delete suspend fun deleteTodoItem(todoItem: TodoItem)

    @Query("SELECT * FROM $TODO_ITEM_TABLE_NAME")
    suspend fun loadTodoItems(): List<TodoItem>
}
