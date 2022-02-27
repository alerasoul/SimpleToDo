package com.example.simpletodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.simpletodoapp.model.ToDo
import com.example.simpletodoapp.repository.ToDoRepository

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ToDoRepository = ToDoRepository(application)
    private var allToDoList: LiveData<List<ToDo>> = repository.getAll()

    suspend fun insert(toDo: ToDo) {
        repository.insert(toDo)
    }

    suspend fun update(toDo: ToDo) {
        repository.update(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        repository.delete(toDo)
    }

    fun getAll(): LiveData<List<ToDo>> {
        return allToDoList
    }

    @Query("SELECT * FROM ToDo WHERE ToDo.categoryId = categoryId")
    fun getAllByCategory(categoryId: Int): LiveData<List<ToDo>> {
        return repository.getAllByCategory(categoryId)
    }


}