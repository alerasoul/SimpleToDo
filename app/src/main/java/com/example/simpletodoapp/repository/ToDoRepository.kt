package com.example.simpletodoapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.simpletodoapp.dao.ToDoDAO
import com.example.simpletodoapp.db.AppDatabase
import com.example.simpletodoapp.model.ToDo

class ToDoRepository(application: Application) {

    private var dao: ToDoDAO
    private var allToDoList: LiveData<List<ToDo>>

    init {
        val database = AppDatabase.getInstance(application)
        dao = database.getToDoDao()
        allToDoList = dao.getAll()
    }

    suspend fun insert(toDo: ToDo) {
        dao.insert(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        dao.delete(toDo)
    }

    suspend fun update(toDo: ToDo) {
        dao.update(toDo)
    }

    fun getAll(): LiveData<List<ToDo>> {
        return allToDoList
    }

    fun getAllByCategory(categoryId: Int): LiveData<List<ToDo>> {
        return dao.getAllByCategory(categoryId)
    }


}