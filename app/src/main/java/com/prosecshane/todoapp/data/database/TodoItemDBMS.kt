package com.prosecshane.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDBMS {
    @Query("SELECT * FROM TodoItem WHERE deleted=0")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE id=:id")
    fun getItemById(id: String): TodoItem

    @Query("SELECT * FROM TodoItem WHERE deleted=0 AND done=0")
    fun getAllUncompleted(): Flow<List<TodoItem>>

    @Query("SELECT COUNT(*) FROM TodoItem WHERE deleted=0 AND done=1")
    fun getNumOfCompleted(): Flow<Int>

    @Query("SELECT * FROM TodoItem WHERE deleted=0")
    suspend fun getAllNoFlow(): List<TodoItem>

    @Insert
    suspend fun insertAll(items: List<TodoItem>)

    @Update
    suspend fun updateItem(todoItem: TodoItem)

    @Insert
    suspend fun insertItem(item: TodoItem)

    @Delete
    suspend fun deleteItem(item: TodoItem)

    @Query("DELETE FROM TodoItem")
    fun deleteTable()
}
