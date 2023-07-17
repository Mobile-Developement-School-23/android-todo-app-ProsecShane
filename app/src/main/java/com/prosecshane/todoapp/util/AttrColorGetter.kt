package com.prosecshane.todoapp.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

// Attribute Color Getter Class
class AttrColorGetter(private val context: Context) {
    // Gets the color ID by attribute name
    @ColorInt
    fun get(@AttrRes colorAttr: Int): Int {
        val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
        return try {
            array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }
}
