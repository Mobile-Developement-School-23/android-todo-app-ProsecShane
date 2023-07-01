package com.prosecshane.todoapp.ui.view
//
//import android.graphics.Paint
//import android.graphics.PorterDuff
//import android.graphics.PorterDuffColorFilter
//import android.os.Build
//import android.view.View
//import android.widget.CheckBox
//import android.widget.TextView
//import androidx.annotation.RequiresApi
//import androidx.core.graphics.drawable.DrawableCompat
//import androidx.core.view.isGone
//import androidx.core.view.isVisible
//import androidx.recyclerview.widget.RecyclerView
//import com.prosecshane.todoapp.R
//import com.prosecshane.todoapp.data.model.Importance
//import com.prosecshane.todoapp.data.model.TodoItem
//import com.prosecshane.todoapp.util.getThemeAttrColor
//import com.prosecshane.todoapp.util.toPx
//import java.text.SimpleDateFormat
//
//// Вью холдер - из данных дела применяет их к превью дела
//class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    private val checkBox: CheckBox = itemView.findViewById(R.id.preview_checkbox)
//    private val text: TextView = itemView.findViewById(R.id.preview_text)
//    private val deadline: TextView = itemView.findViewById(R.id.preview_date)
//
//    // Оформить чекбокс
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun styleCheckBox(done: Boolean, importance: Importance) {
//        if (done) {
//            checkBox.isChecked = true
//            checkBox.buttonDrawable?.colorFilter = PorterDuffColorFilter(
//                getThemeAttrColor(itemView.context, R.attr.colorGreen),
//                PorterDuff.Mode.SRC_ATOP
//            )
//        } else {
//            checkBox.isChecked = false
//            checkBox.buttonDrawable?.colorFilter = PorterDuffColorFilter(
//                getThemeAttrColor(
//                    itemView.context,
//                    if (importance == Importance.HIGH) R.attr.colorRed else R.attr.colorGray),
//                PorterDuff.Mode.SRC_ATOP
//            )
//        }
//    }
//
//    // Оформить текст
//    private fun styleText(textString: String, done: Boolean, importance: Importance) {
//        text.text = if (textString != "") textString else "Без названия"
//        if (done) {
//            text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            text.setTextColor(getThemeAttrColor(itemView.context, R.attr.labelTertiary))
//
//            text.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
//        } else {
//            text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            text.setTextColor(getThemeAttrColor(itemView.context, R.attr.labelPrimary))
//
//            text.compoundDrawablePadding = 5f.toPx.toInt()
//            text.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                when (importance) {
//                    Importance.LOW  -> R.drawable.ic_low_importance
//                    Importance.MID  -> 0
//                    Importance.HIGH -> R.drawable.ic_high_importance
//                },0,0,0
//            )
//            if (importance == Importance.LOW || importance == Importance.HIGH) {
//                DrawableCompat.setTint(
//                    text.compoundDrawablesRelative[0],
//                    getThemeAttrColor(
//                        itemView.context,
//                        if (importance == Importance.LOW) R.attr.colorGray else R.attr.colorRed
//                    )
//                )
//            }
//        }
//    }
//
//    // Главная функция
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun onBind(todoItem: TodoItem) {
//        styleCheckBox(todoItem.done, todoItem.importance)
//        styleText(todoItem.text, todoItem.done, todoItem.importance)
//
//        // оформляем дедлайн
//        if (todoItem.deadline != null) {
//            deadline.isVisible = true
//            deadline.text = SimpleDateFormat("dd.MM.yyyy").format(todoItem.deadline)
//        } else {
//            deadline.isGone = true
//        }
//    }
//}
