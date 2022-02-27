package com.example.simpletodoapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithToDo(
    @Embedded val category: Group.Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val toDo: List<ToDo>,
)