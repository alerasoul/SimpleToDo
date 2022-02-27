package com.example.simpletodoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simpletodoapp.dao.CategoryDAO
import com.example.simpletodoapp.dao.ToDoDAO
import com.example.simpletodoapp.model.Group
import com.example.simpletodoapp.model.ToDo

@Database(entities = [ToDo::class, Group.Category::class], version = 8)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getToDoDao(): ToDoDAO
    abstract fun getCategoryDao(): CategoryDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "db-todo")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase
        }
    }

}