package com.prosecshane.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prosecshane.todoapp.data.database.DatabaseConstants.CURRENT_TABLE_VERSION
import com.prosecshane.todoapp.data.model.TodoItem

@Database(
    version = CURRENT_TABLE_VERSION,
    entities = [
        TodoItem::class,
    ]
)
@TypeConverters(ImportanceConverter::class)
abstract class TodoItemsDatabase : RoomDatabase() {
    abstract fun todoItems() : TodoItemsDao
}
