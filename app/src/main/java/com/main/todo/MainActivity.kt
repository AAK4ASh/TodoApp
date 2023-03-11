package com.main.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.main.todo.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        date()
        initUI()
    }

    private fun initUI() {
        binding.floatingActionButton.setOnClickListener{
            val builder =AlertDialog.Builder(this)
            builder.setTitle("Todo")
            builder.setMessage("message")
            builder.setPositiveButton("Save"){ _, _ -> }
            builder.setNegativeButton("Cancel"){_,_ ->}
            builder.setCancelable(false)
            val dialog= builder.create()
            dialog.show()
        }
    }
    private fun date() {
        val currentDate= LocalDate.now().toString()
        binding.date.text= currentDate
    }
}