package com.example.simpletodoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodoapp.adapter.RecAdapter
import com.example.simpletodoapp.handler.SwipeToDeleteCallback
import com.example.simpletodoapp.model.ToDO
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: com.example.simpletodoapp.viewmodel.ViewModel
    private lateinit var adapter: RecAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var date: TextView
    private lateinit var fabNewToDo: FloatingActionButton
    private var layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        viewModel.getAll().observe(this) {
            adapter.setData(it)
        }

        val addNewResultActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val title = data.getStringExtra(AddOrEditToDoActivity.EXTRA_TITLE)
                    val desc = data.getStringExtra(AddOrEditToDoActivity.EXTRA_DESC)
                    val toDo = ToDO(title = title!!, description = desc!!, done = false)
                    MainScope().launch {
                        viewModel.insert(toDo)
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
                    val toDo = ToDO(id, title!!, desc!!, false)
                    MainScope().launch {
                        viewModel.update(toDo)
                    }
                }
            }

        fabNewToDo.setOnClickListener {
            val intent = Intent(this, AddOrEditToDoActivity::class.java)
            addNewResultActivity.launch(intent)
        }

        adapter.setOnItemClickListener(object : RecAdapter.OnItemClickListener {
            override fun onItemClick(toDO: ToDO) {
                val intent = Intent(this@MainActivity, AddOrEditToDoActivity::class.java)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_TITLE, toDO.title)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_DESC, toDO.description)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_ID, toDO.id)
                editResultActivity.launch(intent)
            }
        })

        adapter.setOnStateChangeListener(object : RecAdapter.OnDoneStateChangeListener {
            override fun onStateChanged(toDO: ToDO, isChecked: Boolean) {
                toDO.done = isChecked
                MainScope().launch {
                    viewModel.update(toDO)
                }
            }
        })

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MainScope().launch {
                    viewModel.delete(adapter.getToDo(viewHolder.adapterPosition))
                    Toast.makeText(this@MainActivity, "ToDo item deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)

    }

    private fun init() {
        bindViews()
        adapter = RecAdapter(this)
        viewModel =
            ViewModelProvider(this).get(com.example.simpletodoapp.viewmodel.ViewModel::class.java)
    }

    private fun bindViews() {
        recycler = findViewById(R.id.rec)
        fabNewToDo = findViewById(R.id.fab)
        date = findViewById(R.id.dateTxt)
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate = sdf.format(Date())
        date.text = currentDate
    }
}