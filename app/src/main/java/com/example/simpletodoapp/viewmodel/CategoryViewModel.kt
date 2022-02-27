package com.example.simpletodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simpletodoapp.model.Group
import com.example.simpletodoapp.repository.CategoryRepository


class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: CategoryRepository = CategoryRepository(application)
    private var allCategoriesList: LiveData<List<Group.Category>> = repository.getAll()

    suspend fun insert(category: Group.Category) {
        repository.insert(category)
    }

    suspend fun update(category: Group.Category) {
        repository.update(category)
    }

    suspend fun delete(category: Group.Category) {
        repository.delete(category)
    }

    fun getAll(): LiveData<List<Group.Category>> {
        return allCategoriesList
    }
}