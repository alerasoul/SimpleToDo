package com.example.simpletodoapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodoapp.adapter.CategoryGroupAdapter
import com.example.simpletodoapp.model.Group
import com.example.simpletodoapp.util.RandomColor
import com.example.simpletodoapp.viewmodel.CategoryViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var groupAdapter: CategoryGroupAdapter
    private lateinit var recycler: RecyclerView
    private var layoutManager = GridLayoutManager(this, 3)
    private lateinit var date: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        categoryViewModel.getAll().observe(this) {
            groupAdapter.setData(it)
            groupAdapter.notifyDataSetChanged()
        }

        groupAdapter.setOnItemClickListener(object : CategoryGroupAdapter.OnItemClickListener {
            override fun onItemClick(category: Group.Category) {
                var intent = Intent(this@MainActivity, CategoryActivity::class.java)
                intent.putExtra("catTitle", category.title)
                intent.putExtra("catId", category.id)
                startActivity(intent)
            }
        })
        groupAdapter.setOnItemClickListenerNew(object :
            CategoryGroupAdapter.OnItemClickListenerNew {
            override fun onItemClick(newCategory: Group.NewCategory) {
                newCategoryDialog()
            }
        })

    }

    fun newCategoryDialog() {
        var catTitle = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("New Category")

        val input = EditText(this@MainActivity)
        input.hint = "Enter category title"
        input.inputType = InputType.TYPE_CLASS_TEXT

        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            catTitle = input.text.toString()
            MainScope().launch {
                if (catTitle.isNotEmpty()) {
                    val category =
                        Group.Category(title = catTitle, color = RandomColor.generateRandomColor())
                    categoryViewModel.insert(category)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }


    private fun init() {
        bindViews()
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate = sdf.format(Date())
        date.text = currentDate
        groupAdapter = CategoryGroupAdapter(this)
        categoryViewModel =
            ViewModelProvider(this).get(CategoryViewModel::class.java)
        recycler.layoutManager = layoutManager
        recycler.adapter = groupAdapter

    }

    private fun bindViews() {
        date = findViewById(R.id.dateTxt)
        recycler = findViewById(R.id.categoryRec)
    }

}