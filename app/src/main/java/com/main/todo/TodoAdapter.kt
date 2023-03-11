package com.main.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//adapter class to bind data with UI
class TodoAdapter (private val todos:MutableList<Todo>):
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() { //each of the column in the RV is view-holder
    class TodoViewHolder (itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_task_item ,parent,false))//inflating the recylertaskitem
    }
    override fun getItemCount(): Int {
return todos.size   }
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo =todos.get(position)
        holder.itemView.apply {
        }

    }


}
