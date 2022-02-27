package com.example.simpletodoapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.simpletodoapp.dao.CategoryDAO
import com.example.simpletodoapp.db.AppDatabase
import com.example.simpletodoapp.model.Group

class CategoryRepository(application: Application) {

    private var dao: CategoryDAO
    private var allCategoriesList: LiveData<List<Group.Category>>

    init {
        val database = AppDatabase.getInstance(application)
        dao = database.getCategoryDao()
        allCategoriesList = dao.getAll()
    }

    suspend fun insert(category: Group.Category) {
        dao.insert(category)
    }

    suspend fun delete(category: Group.Category) {
        dao.delete(category)
    }

    suspend fun update(category: Group.Category) {
        dao.update(category)
    }

    fun getAll(): LiveData<List<Group.Category>> {
        return allCategoriesList
    }


}