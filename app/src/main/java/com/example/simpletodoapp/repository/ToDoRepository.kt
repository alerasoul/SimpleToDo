package com.example.simpletodoapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.simpletodoapp.dao.ToDoDAO
import com.example.simpletodoapp.db.ToDoDatabase
import com.example.simpletodoapp.model.ToDO

class ToDoRepository(application: Application) {

    private var dao: ToDoDAO
    private var allToDoList: LiveData<List<ToDO>>

    init {
        val database = ToDoDatabase.getInstance(application)
        dao = database.getDao()
        allToDoList = dao.getAll()
    }

    suspend fun insert(toDO: ToDO) {
        dao.insert(toDO)
    }

    suspend fun delete(toDO: ToDO) {
        dao.delete(toDO)
    }

    suspend fun update(toDO: ToDO) {
        dao.update(toDO)
    }

    fun getAll(): LiveData<List<ToDO>> {
        return allToDoList
    }


}