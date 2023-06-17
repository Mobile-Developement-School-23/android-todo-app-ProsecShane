package com.prosecshane.todoapp.util

import android.content.res.Resources
import android.util.TypedValue

// Расширение для преобразования числа в пиксели
val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)
