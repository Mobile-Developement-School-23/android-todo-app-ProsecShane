package com.prosecshane.todoapp.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.prosecshane.todoapp.data.model.Importance
import java.util.UnknownFormatConversionException

class ImportanceConverter {
    @TypeConverter
    fun importanceToString(i: Importance): String = when (i) {
        Importance.LOW  -> "low"
        Importance.MID  -> "basic"
        Importance.HIGH -> "important"
    }

    @TypeConverter
    fun stringToImportance(s: String): Importance = when (s) {
        "low"       -> Importance.LOW
        "basic"     -> Importance.MID
        "important" -> Importance.HIGH
        else -> throw UnknownFormatConversionException("Could not convert $s to Importance")
    }
}
