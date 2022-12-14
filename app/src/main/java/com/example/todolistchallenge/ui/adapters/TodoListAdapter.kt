package com.example.todolistchallenge.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistchallenge.R
import com.example.todolistchallenge.database.models.TodoEntity
import com.example.todolistchallenge.databinding.TodoItemBinding

interface TodoItemListener{
    fun onItemSelected(position: Int): Boolean
}

class TodoListAdapter(private val todo: List<TodoEntity>) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {


    var onLongItemClick: ((entity: TodoEntity) -> Unit)? = null

    var onItemClick: ((entity: TodoEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bindView(todo[position])
    }

    override fun getItemCount() = todo.size

    inner class TodoListViewHolder(binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val view: View = binding.root
        private val tvTodoTitle: TextView = binding.title
        private val checkBox: CheckBox = binding.checkBoxList
//        private val tvStatusTodo: TextView = binding.statusTodo
        private val tvDate: TextView = binding.date
        private val imgCard: ImageView = binding.colorCardTodoItem


        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindView(todo: TodoEntity) {
            tvTodoTitle.text = todo.title
            changeCheckBox(checkBox, todo.done)
//            changeTextDone(tvStatusTodo, todo.done)

            tvDate.text = todo.createdDate
            when (todo.done) {
                1 -> imgCard.background = itemView.resources.getDrawable(R.drawable.shape_done_card)
                else -> imgCard.background = itemView.resources.getDrawable(R.drawable.shape_todo_card)
            }

            itemView.setOnLongClickListener{
                 onLongItemClick?.invoke(todo)
                true
            }

            itemView.setOnClickListener{
                onItemClick?.invoke(todo)
//                changeCheckBox(checkBox, todo.done)
                checkBox.isChecked = true
                imgCard.background = itemView.resources.getDrawable(R.drawable.shape_done_card)
//                changeTextDone(tvStatusTodo, todo.done)
//                tvStatusTodo.text = when (todo.done) {
//                    1 -> "Done"
//                    else -> "To do"
//                }
            }

        }

        private fun changeCheckBox(checkBox: CheckBox, done: Int){
            when(done){
                1 -> checkBox.isChecked = true
                else -> checkBox.isChecked = false
            }

//            textView.text = when (done) {
//                1 -> "Done"
//                else -> "To do"
//            }
        }
    }
}