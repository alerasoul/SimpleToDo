package com.example.simpletodoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodoapp.adapter.ToDoAdapter
import com.example.simpletodoapp.handler.SwipeToDeleteCallback
import com.example.simpletodoapp.model.ToDo
import com.example.simpletodoapp.viewmodel.ToDoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class CategoryActivity : AppCompatActivity() {
    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var adapter: ToDoAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var catTitle: TextView
    private lateinit var btnBack: ImageView
    private lateinit var fabNewToDo: FloatingActionButton
    private var layoutManager = LinearLayoutManager(this)
    private var categoryId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        init()

        if (categoryId == 0) {
            toDoViewModel.getAll().observe(this) {
                adapter.setData(it)
            }
        } else {
            toDoViewModel.getAllByCategory(categoryId).observe(this) {
                adapter.setData(it)
            }
        }

        val addNewResultActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val title = data.getStringExtra(AddOrEditToDoActivity.EXTRA_TITLE)
                    val desc = data.getStringExtra(AddOrEditToDoActivity.EXTRA_DESC)
                    val toDo = ToDo(title = title!!,
                        description = desc!!,
                        done = false,
                        categoryId = categoryId)
                    MainScope().launch {
                        toDoViewModel.insert(toDo)
                    }
                }
            }

        val editResultActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val id = data.getIntExtra(AddOrEditToDoActivity.EXTRA_ID, -1)
                    if (id == -1) {
                        Toast.makeText(this, "Invalid Data!", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }
                    val title = data.getStringExtra(AddOrEditToDoActivity.EXTRA_TITLE)
                    val desc = data.getStringExtra(AddOrEditToDoActivity.EXTRA_DESC)
                    val toDo = ToDo(id, title!!, desc!!, false, categoryId)
                    MainScope().launch {
                        toDoViewModel.update(toDo)
                    }
                }
            }

        btnBack.setOnClickListener {
            finish()
        }

        fabNewToDo.setOnClickListener {
            val intent = Intent(this, AddOrEditToDoActivity::class.java)
            addNewResultActivity.launch(intent)
        }

        adapter.setOnItemClickListener(object : ToDoAdapter.OnItemClickListener {
            override fun onItemClick(toDo: ToDo) {
                val intent = Intent(this@CategoryActivity, AddOrEditToDoActivity::class.java)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_TITLE, toDo.title)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_DESC, toDo.description)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_ID, toDo.id)
                editResultActivity.launch(intent)
            }
        })

        adapter.setOnStateChangeListener(object : ToDoAdapter.OnDoneStateChangeListener {
            override fun onStateChanged(toDo: ToDo, isChecked: Boolean) {
                toDo.done = isChecked
                MainScope().launch {
                    toDoViewModel.update(toDo)
                }
            }
        })

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MainScope().launch {
                    toDoViewModel.delete(adapter.getToDo(viewHolder.adapterPosition))
                    Toast.makeText(this@CategoryActivity, "ToDo item deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun init() {
        bindViews()
        var categoryTitle = intent.getStringExtra("catTitle")
        catTitle.text = categoryTitle
        adapter = ToDoAdapter(this)
        toDoViewModel =
            ViewModelProvider(this).get(com.example.simpletodoapp.viewmodel.ToDoViewModel::class.java)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        categoryId = intent.getIntExtra("catId", -1)
    }

    private fun bindViews() {
        recycler = findViewById(R.id.categoryRec)
        fabNewToDo = findViewById(R.id.fab)
        catTitle = findViewById(R.id.catTitle)
        btnBack = findViewById(R.id.back)
    }

}