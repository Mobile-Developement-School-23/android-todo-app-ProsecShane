package com.prosecshane.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

// Data class for a single item
@Entity
@Serializable
data class TodoItem(
    @PrimaryKey                 var id:String               = UUID.randomUUID().toString(),
    @SerialName("done")         var done: Boolean           = false,
                                var text: String            = "",
    @SerialName("importance")   var importance: Importance  = Importance.MID,
    @SerialName("deadline")     var deadline: Long?         = null,
    @SerialName("created_at")   var createdOn: Long         = System.currentTimeMillis(),
    @SerialName("changed_at")   var editedOn: Long          = createdOn,

    @SerialName("last_updated_by")  var lastUpdatedBy: String   = "dev",
    @Transient                      var deleted: Boolean        = false,
                                    var color: String?          = null,
)
