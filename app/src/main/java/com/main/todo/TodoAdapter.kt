package com.main.todo

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.main.todo.Constants.PREF_ARRAY
import com.main.todo.Constants.PREF_NAME
import kotlinx.android.synthetic.main.recycler_task_item.view.*
import org.json.JSONArray
import org.json.JSONObject

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
            taskName.text= currentTodo.todoName
            checkbox.isChecked= currentTodo.isChecked
            toggleStrikeThrough(tvTodo = taskName,currentTodo.isChecked)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(taskName, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
                updateLocalData(this.context, currentTodo, isChecked)

            }

        }

    }

    private fun updateLocalData(context: Context,currentTodo: Todo, isChecked: Boolean) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingArray = sharedPref.getString(PREF_ARRAY, "[]" ?: "[]")//gets saved array of todos
        val array = JSONArray(existingArray)//to json array
        val newArray = JSONArray()
        for (i in 0 until array.length()) {//loops over all avilable todos
            val json = array.get(i) as? JSONObject//each todos as jsonobject
            json?.let { //null safety check
                val currentItemId = it.getString("id")
                if (currentItemId === currentTodo.id) {
                    it.put("isChecked", isChecked)
                }
                newArray.put(json)
            }
        }
        val editor = sharedPref.edit()
        editor.putString(PREF_ARRAY,newArray.toString())
        editor.apply()
    }





    private fun toggleStrikeThrough(tvTodo: TextView, isChecked: Boolean) {
        if (isChecked){
            tvTodo.paintFlags = tvTodo.paintFlags or STRIKE_THRU_TEXT_FLAG//or and - bitwise operations
        }else{
            tvTodo.paintFlags=tvTodo.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }

    }

    fun addTodo(todo: Todo) {
             todos.add(todo)
             notifyItemInserted(todos.size-1)
    }


}
