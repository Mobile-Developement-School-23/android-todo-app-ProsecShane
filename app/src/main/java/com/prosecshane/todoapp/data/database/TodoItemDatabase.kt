package com.prosecshane.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prosecshane.todoapp.data.Constants.DATABASE_NAME
import com.prosecshane.todoapp.data.Constants.DATABASE_VERSION
import com.prosecshane.todoapp.data.model.TodoItem

@Database(entities = [TodoItem::class], version = DATABASE_VERSION)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract fun todoItemDBMS(): TodoItemDBMS

    companion object {
        @Volatile
        private var instance: TodoItemDatabase? = null

        fun getDatabase(context: Context): TodoItemDatabase {
            return instance ?: synchronized(this) {
                val _instance = Room.databaseBuilder(
                    context,
                    TodoItemDatabase::class.java,
                    DATABASE_NAME
                ).build()
                instance = _instance
                _instance
            }
        }
    }
}
