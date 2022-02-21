package com.example.simpletodoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.simpletodoapp.model.ToDO

@Dao
interface ToDoDAO {

    @Insert
    suspend fun insert(vararg toDo:ToDO)

    @Delete
    suspend fun delete(toDo: ToDO)

    @Update
    suspend fun update(vararg toDo: ToDO)

    @Query("SELECT * FROM ToDO")
    fun getAll():LiveData<List<ToDO>>

}