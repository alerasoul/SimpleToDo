package com.example.simpletodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simpletodoapp.model.ToDO
import com.example.simpletodoapp.repository.ToDoRepository

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ToDoRepository = ToDoRepository(application)
    private var allToDoList: LiveData<List<ToDO>> = repository.getAll()

    suspend fun insert(toDo: ToDO) {
        repository.insert(toDo)
    }

    suspend fun update(toDo: ToDO) {
        repository.update(toDo)
    }

    suspend fun delete(toDo: ToDO) {
        repository.delete(toDo)
    }

    fun getAll(): LiveData<List<ToDO>> {
        return allToDoList
    }
}