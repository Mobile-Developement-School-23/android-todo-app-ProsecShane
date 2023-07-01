package com.prosecshane.todoapp.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

// Get Color Int by Color Attribute
@ColorInt
fun getThemeAttrColor(context: Context, @AttrRes colorAttr: Int): Int {
    val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
    return try {
        array.getColor(0, 0)
    } finally {
        array.recycle()
    }
}
