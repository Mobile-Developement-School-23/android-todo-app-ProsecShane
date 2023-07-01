package com.prosecshane.todoapp.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerOffsetItemDecoration(
    private val startOffset: Int = 0,
    private val topOffset: Int = 0,
    private val endOffset: Int = 0,
    private val bottomOffset: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(startOffset, topOffset, endOffset, bottomOffset)
    }
}
