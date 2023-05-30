package com.main.todo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.todo.Constants.Companion.PREF_ARRAY
import com.main.todo.Constants.Companion.PREF_NAME
import com.main.todo.databinding.ActivityMainBinding
import com.main.todo.databinding.RecyclerTaskItemBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bind:RecyclerTaskItemBinding
    private lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initUI()
        deleteBtn()

    }

    private fun deleteBtn() {

            bind.viewHolder.setOnClickListener{
                val task= mutableListOf(todoAdapter).get(0)
                val builder=AlertDialog.Builder(this)
                builder.setCancelable(true)
                builder.setNegativeButton("Delete"){_,_ ->
                    todoAdapter.deleteDoneTodos(todo )
                }
            }
        }


    private fun initUI() {
       getAllTasks()
        currentDate()
        binding.floatingActionButton.setOnClickListener{
showInputDialog()
        }
    }


    private fun getAllTasks() {
        val sharedPreferences= getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val existingArray = sharedPreferences.getString(PREF_ARRAY,"[]")?:"[]"//a ternary operator
        val array = JSONArray(existingArray) //array to json array
        val list= mutableListOf<Todo>()
        for (i in 0 until array.length()){
            val jsonString=array.get(i)
            val json=when(jsonString){
                is JSONObject->jsonString
                else->(jsonString as? String)?.let { JSONObject(it) }
            }
            json?.let{
                val model = Gson().fromJson(json.toString(),Todo::class.java)
                list.add(model)
            }
        }
  todoAdapter= TodoAdapter(list,this)  //instantiate TodoAdapter
        binding.recyclerTasks.layoutManager=LinearLayoutManager(this)//linearlayout manager
        binding.recyclerTasks.adapter= todoAdapter  //set the adapter of recycler view
    }

    private fun showInputDialog() {
        val builder =AlertDialog.Builder(this)
        builder.setTitle("Enter your task")
        val layout = FrameLayout(this)
        layout.setPaddingRelative(45,15,45,15)
        val editText= EditText(this)
        editText.hint="Enter a task"
        editText.maxLines=1
        layout.addView(editText)//adding the view to the layout
        builder.setView(layout)//adding layout to builder
        builder.setPositiveButton("Save"){ _, _ ->
           val task=editText.text.toString()
            if (task.isNotEmpty()){
                saveTask(task)
            }
                   }
        builder.setNegativeButton("Cancel"){_,_ ->}
        builder.setCancelable(false)
        val dialog= builder.create()
        dialog.show()
    }
    private fun currentDate() {
        val currentDate= SimpleDateFormat("EEE,MMM,dd",Locale.US)
        binding.date.text=currentDate.format(Date())
    }


    private fun saveTask(task:String) {
val todo =Todo(task,false,UUID.randomUUID().toString())//generates a random UUID (Universally Unique Identifier)
        todoAdapter.addTodo(todo)
        val jsonObject=JSONObject()
        jsonObject.put("task",task)
        jsonObject.put("isCompleted",false)
        jsonObject.put("id",UUID.randomUUID().toString().replace("-","").uppercase())
        //replace to remove - , and uppercase to convert string to uppercase

        val sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE)
        val existingArray = sharedPreferences.getString(PREF_ARRAY, "[]") ?: "[]"   //getString() retrieves string values from a shared preferences
        val array = JSONArray(existingArray)//converting string t json array
        //converting data class to json to save (use Gson library)
        val todoJson = JSONObject(Gson().toJson(todo))
        //adding element to array using  put()
        array.put(todoJson.toString())
        val editor =sharedPreferences.edit()
        //putstring() writes string values to a shared pref file
        editor.putString(PREF_ARRAY,array.toString())
        editor.apply() //apply() save the changes to shared pref files
    }
}

