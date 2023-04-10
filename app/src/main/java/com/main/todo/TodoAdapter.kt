package com.main.todo

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.main.todo.Constants.Companion.PREF_ARRAY
import com.main.todo.Constants.Companion.PREF_NAME
import com.main.todo.databinding.RecyclerTaskItemBinding
import org.json.JSONArray
import org.json.JSONObject

//adapter class to bind data with UI
class TodoAdapter(private val todos: MutableList<Todo>, private val context: Context):

    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() { //each of the column in the RV is view-holder
    inner class  TodoViewHolder(private val binding: RecyclerTaskItemBinding):RecyclerView.ViewHolder(binding.root){
        val taskName = binding.taskName
        val checkBox = binding.checkbox
        private val viewHolder= binding.viewHolder
        init {
            viewHolder.setOnClickListener(){
                val task= todos[layoutPosition]
                val builder=AlertDialog.Builder(context)
                builder.setCancelable(true)
                builder.setNegativeButton("Delete"){_,_ ->
                deleteDoneTodos(task)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding= RecyclerTaskItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
return todos.size   }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo =todos.get(position)
        holder.apply {
            taskName.text= currentTodo.todoName
            checkBox.isChecked= currentTodo.isChecked
            toggleStrikeThrough(tvTodo = taskName,currentTodo.isChecked)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(taskName, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
                updateLocalData(currentTodo, isChecked)


            }

        }

    }
    private fun updateLocalData(currentTodo: Todo,isChecked: Boolean){
        val sharedPref=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        val existingArray=sharedPref.getString(PREF_ARRAY,"[]")?:"[]"
        val array=JSONArray(existingArray)
        val newArray=JSONArray()
        for (i in 0 until array.length()){
            val json =(array.get(i)as?String)?.let { JSONObject(it) }
            val currentItemId = json?.getString("id")
            if (currentItemId==currentTodo.id){
                json?.let { newArray.put(json.toString()) }
            }
            val editor= sharedPref.edit()
            editor.putString(PREF_ARRAY,newArray.toString())
            editor.apply()
        }

    }


    private fun toggleStrikeThrough(tvTodo: TextView, isChecked: Boolean) {
        if (isChecked){
            tvTodo.paintFlags = tvTodo.paintFlags or STRIKE_THRU_TEXT_FLAG//or and - bitwise operations
        }else{
            tvTodo.paintFlags=tvTodo.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }

    }
  fun deleteDoneTodos(todo: Todo){
        todos.removeAll{todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    fun addTodo(todo: Todo) {
             todos.add(todo)
             notifyItemInserted(todos.size-1)
    }
}

