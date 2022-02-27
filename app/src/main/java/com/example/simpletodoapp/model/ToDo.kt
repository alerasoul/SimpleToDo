package com.example.simpletodoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var done: Boolean,
    var categoryId: Int,
)