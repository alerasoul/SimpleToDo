package com.example.simpletodoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.simpletodoapp.model.Group

@Dao
interface CategoryDAO {

    @Insert
    suspend fun insert(vararg category: Group.Category)

    @Delete
    suspend fun delete(category: Group.Category)

    @Update
    suspend fun update(vararg category: Group.Category)

    @Query("SELECT * FROM Category")
    fun getAll(): LiveData<List<Group.Category>>

}