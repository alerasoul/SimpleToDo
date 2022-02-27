package com.example.simpletodoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.simpletodoapp.model.ToDo

@Dao
interface ToDoDAO {

    @Insert
    suspend fun insert(vararg toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)

    @Update
    suspend fun update(vararg toDo: ToDo)

    @Query("SELECT * FROM ToDo")
    fun getAll(): LiveData<List<ToDo>>

    @Query("SELECT * FROM ToDo WHERE ToDo.categoryId = :categoryId")
    fun getAllByCategory(categoryId: Int): LiveData<List<ToDo>>

}