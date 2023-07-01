package com.prosecshane.todoapp.ui.view
//
//import android.os.Build
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.LinearLayout
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.prosecshane.todoapp.R
//import com.prosecshane.todoapp.data.model.TodoItem
//
//// Адаптер
//
//class PreviewAdapter(
//    private val checkBoxListener: (position: Int) -> Unit = {},
//    private val clickableListener: (position: Int) -> Unit = {}
//) : RecyclerView.Adapter<PreviewViewHolder>() {
//    // Обновляемый список
//    var items = listOf<TodoItem>()
//        set (value) {
//            val callback = PreviewCallbackImpl(
//                oldItems = field,
//                newItems = value,
//                areItemsTheSameImpl = { oldItem: TodoItem, newItem: TodoItem ->
//                    oldItem.id == newItem.id
//                },
//                areContentsTheSameImpl = { oldItem: TodoItem, newItem: TodoItem ->
//                            oldItem.text == newItem.text &&
//                            oldItem.importance == newItem.importance &&
//                            oldItem.deadline == newItem.deadline &&
//                            oldItem.done == newItem.done
//                }
//            )
//            val diffResult = DiffUtil.calculateDiff(callback)
//            field = value
//            diffResult.dispatchUpdatesTo(this)
//            // Честно не знаю, почему ДиффУтиль не работает
//            // очень долго пытался пофиксить, но так и не понял, в чем проблема
//            // надеюсь на понимание)
//            notifyDataSetChanged() // TODO: fix DiffUtil
//        }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return PreviewViewHolder(layoutInflater.inflate(
//            R.layout.todo_item_preview,
//            parent,
//            false
//        ))
//    }
//
//    // Прикрепление элементов с установкой onClickListener-ов
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
//        holder.onBind(items[position])
//
//        val clickable: LinearLayout = holder.itemView.findViewById(R.id.preview_clickable)
//        clickable.setOnClickListener {
//            clickableListener(position)
//        }
//
//        val checkBox: CheckBox = holder.itemView.findViewById(R.id.preview_checkbox)
//        checkBox.setOnClickListener {
//            checkBox.isChecked = !checkBox.isChecked
//            checkBoxListener(position)
//        }
//    }
//}
