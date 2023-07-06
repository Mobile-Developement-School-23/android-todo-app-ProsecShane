package com.prosecshane.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prosecshane.todoapp.data.database.DatabaseConstants.TODO_ITEM_TABLE_NAME
import java.util.UUID

// Data class for a single item
@Entity(tableName = TODO_ITEM_TABLE_NAME)
data class TodoItem(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id:             String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "done")
    val done:           Boolean = false,

    @ColumnInfo(name = "text")
    val text:           String = "",

    @ColumnInfo(name = "importance")
    val importance:     Importance = Importance.MID,

    @ColumnInfo(name = "deadline")
    val deadline:       Long? = null,

    @ColumnInfo(name = "created_at")
    val createdOn:      Long = System.currentTimeMillis(),

    @ColumnInfo(name = "changed_at")
    val editedOn:       Long = createdOn,

    @ColumnInfo(name = "last_updated_by")
    val deviceId:       String,
)
