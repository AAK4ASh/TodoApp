package com.main.todo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.todo.Constants.PREF_ARRAY
import com.main.todo.Constants.PREF_NAME
import com.main.todo.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initUI()
    }

    private fun initUI() {
        getAllTasks()
        date()
        binding.floatingActionButton.setOnClickListener{
showInputDialog()
        }
    }

    private fun getAllTasks() {
        val sharedPreferences= getSharedPreferences(PREF_ARRAY, MODE_PRIVATE)
        val existingArray = sharedPreferences.getString(PREF_ARRAY,"[]")?:"[]"//a ternary operator
        val array = JSONArray(existingArray) //array to json array
        val list= mutableListOf<Todo>()
        for (i in 0 until array.length()){
        val jsonObject= array.get(i)as?JSONObject //as? used to handle cases where the retrieved object is not a JSONObject
             jsonObject.let { val model = Gson().fromJson<Todo>(it.toString(),Todo::class.java)
                list.add(model)
            }
        }
  todoAdapter= TodoAdapter(list)  //instantiate TodoAdapter
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
            saveTask(task=String())
        }
        builder.setNegativeButton("Cancel"){_,_ ->}
        builder.setCancelable(false)
        val dialog= builder.create()
        dialog.show()
    }
    private fun date() {
        val currentDate= LocalDate.now().toString()
        binding.date.text= currentDate
    }


    private fun saveTask(task:String) {
val todo =Todo(task,false,UUID.randomUUID().toString())//generates a random UUID (Universally Unique Identifier)
        todoAdapter.addTodo(todo)

        val sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE)
        val existingArray = sharedPreferences.getString(PREF_ARRAY, "[]") ?: "[]"   //getString() retrieves string values from a shared preferences
        val array = JSONArray(existingArray)//converting string t json array
        //converting data class to json to save (use Gson library)
        val todoJson = JSONObject(Gson().toJson(todo))
        //adding element to array using  put()
        array.put(todoJson)
        val editor =sharedPreferences.edit()
        //putstring() writes string values to a shared pref file
        editor.putString(PREF_ARRAY,array.toString())
        editor.apply() //apply() save the changes to shared pref files
    }


}

