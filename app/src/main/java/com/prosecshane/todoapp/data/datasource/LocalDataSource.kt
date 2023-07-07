package com.prosecshane.todoapp.data.datasource

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.prosecshane.todoapp.data.database.DatabaseConstants.TABLE_NAME
import com.prosecshane.todoapp.data.database.TodoItemsDatabase
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ui.App

class LocalDataSource : TodoItemsDataSource {
    lateinit var database: TodoItemsDatabase

    override suspend fun loadTodoItems(): List<TodoItem> {
        if (!this::database.isInitialized) buildDatabase()
        return database.todoItems().loadTodoItems()
    }

    private fun buildDatabase() {
        database = Room.databaseBuilder(
            App.getApplicationContext(), TodoItemsDatabase::class.java, TABLE_NAME
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("todoapp_debug_tag", "onCreate")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d("todoapp_debug_tag", "onOpen")
                }
            }
        ).build()
    }
}
