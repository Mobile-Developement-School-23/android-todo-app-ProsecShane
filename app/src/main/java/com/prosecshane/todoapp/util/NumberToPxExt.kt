package com.prosecshane.todoapp.util

import android.content.res.Resources
import android.util.TypedValue

// Number to Px extension
val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)
