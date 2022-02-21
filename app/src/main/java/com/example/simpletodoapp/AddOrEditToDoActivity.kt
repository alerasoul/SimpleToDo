package com.example.simpletodoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AddOrEditToDoActivity : AppCompatActivity() {

    private lateinit var btnAddEdit: Button
    private lateinit var btnBack: ImageView
    private lateinit var titleEdit: EditText
    private lateinit var descEdit: EditText
    private lateinit var titleTxt: TextView

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESC = "EXTRA_DESC"
        const val EXTRA_ID = "EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_to_do)
        bindViews()
        btnBack.setOnClickListener {
            finish()
        }
        btnAddEdit.setOnClickListener {
            val title = titleEdit.text.toString()
            val desc = descEdit.text.toString()
            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Enter the values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val data = Intent()
            data.putExtra(EXTRA_TITLE, title)
            data.putExtra(EXTRA_DESC, desc)

            val id = intent.extras?.getInt(EXTRA_ID, -1)
            if (id != -1)
                data.putExtra(EXTRA_ID, id)

            setResult(RESULT_OK, data)
            finish()
        }
        val inputIntent = intent
        if (inputIntent.hasExtra(EXTRA_ID)) {
            titleTxt.text = getString(R.string.editTit)
            btnAddEdit.text = getString(R.string.editBtn)
            titleEdit.setText(inputIntent.getStringExtra(EXTRA_TITLE))
            descEdit.setText(inputIntent.getStringExtra(EXTRA_DESC))
        } else {
            titleTxt.text = getString(R.string.addTit)
            btnAddEdit.text = getString(R.string.addBtn)
        }
    }

    private fun bindViews() {
        titleTxt = findViewById(R.id.title)
        titleEdit = findViewById(R.id.titleEdit)
        descEdit = findViewById(R.id.descEdit)
        btnAddEdit = findViewById(R.id.btnAddEdit)
        btnBack = findViewById(R.id.back)
    }

}