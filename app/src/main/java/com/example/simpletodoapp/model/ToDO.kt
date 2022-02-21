package com.example.simpletodoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ToDO")
data class ToDO(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var done: Boolean,
)