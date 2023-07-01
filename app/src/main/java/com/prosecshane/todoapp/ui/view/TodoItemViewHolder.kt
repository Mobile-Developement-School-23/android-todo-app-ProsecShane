package com.prosecshane.todoapp.ui.view

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.prosecshane.todoapp.R
import com.prosecshane.todoapp.data.model.Importance
import com.prosecshane.todoapp.data.model.TodoItem
import com.prosecshane.todoapp.ui.stateholders.TodoItemsViewModel
import com.prosecshane.todoapp.util.getThemeAttrColor
import com.prosecshane.todoapp.util.toPx
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ViewHolder for the items in the RecyclerView
@RequiresApi(Build.VERSION_CODES.M)
class TodoItemViewHolder(
    itemView: View,
    private val viewModel: TodoItemsViewModel,
    private val navController: NavController
) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.preview_text)
    private val deadline: TextView = itemView.findViewById(R.id.preview_date)
    private val checkBox: CheckBox = itemView.findViewById(R.id.preview_checkbox)
    private val clickable: LinearLayout = itemView.findViewById(R.id.preview_clickable)

    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.US)

    // Setup item text
    private fun bindTextView(text: String, importance: Importance, done: Boolean) {
        textView.text = if (text != "") text else itemView.context.getString(R.string.preview_text_placeholder)
        textView.compoundDrawablePadding = 5f.toPx.toInt()
        if (done) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            textView.setTextColor(getThemeAttrColor(itemView.context, R.attr.labelTertiary))
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            textView.setTextColor(getThemeAttrColor(itemView.context, R.attr.labelPrimary))
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                when (importance) {
                    Importance.LOW  -> R.drawable.ic_low_importance
                    Importance.MID  -> 0
                    Importance.HIGH -> R.drawable.ic_high_importance
                },0,0,0
            )
            if (importance == Importance.LOW || importance == Importance.HIGH) {
                DrawableCompat.setTint(
                    textView.compoundDrawablesRelative[0],
                    getThemeAttrColor(
                        itemView.context,
                        if (importance == Importance.LOW) R.attr.colorGray else R.attr.colorRed
                    )
                )
            }
        }
    }

    // Setup item checkbox
    private fun bindCheckBox(done: Boolean, importance: Importance) {
        checkBox.isChecked = done
        checkBox.buttonDrawable?.colorFilter = PorterDuffColorFilter(
            getThemeAttrColor(
                itemView.context,
                if (done) R.attr.colorGreen
                else (if (importance == Importance.HIGH) R.attr.colorRed else R.attr.colorGray)
            ), PorterDuff.Mode.SRC_ATOP
        )
    }

    // Setup item deadline
    private fun bindDeadline(deadlineValue: Long?) {
        if (deadlineValue == null) {
            deadline.isGone = true
        } else {
            deadline.isVisible = true
            deadline.text = dateFormat.format(Date(deadlineValue))
        }
    }

    // Setup every item
    fun bind(todoItem: TodoItem) {
        // methods from above
        bindTextView(todoItem.text, todoItem.importance, todoItem.done)
        bindCheckBox(todoItem.done, todoItem.importance)
        bindDeadline(todoItem.deadline)

        // Onclick Listeners
        checkBox.setOnClickListener {
            viewModel.onTodoItemChanged(todoItem.copy(done = !todoItem.done))
        }
        clickable.setOnClickListener {
            viewModel.setCurrentTodoItem(todoItem)
            navController.navigate(R.id.action_fraglist_to_fragitem)
        }
    }
}
