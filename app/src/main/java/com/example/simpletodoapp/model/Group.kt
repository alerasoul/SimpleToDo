package com.example.simpletodoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

sealed class Group {
    @Entity(tableName = "Category")
    data class Category(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var title: String,
        var color: Int,
    ) : Group()

    data class NewCategory(
        var title: String,
        var image: Int,
    ) : Group()
}