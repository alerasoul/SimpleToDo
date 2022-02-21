package com.example.simpletodoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simpletodoapp.dao.ToDoDAO
import com.example.simpletodoapp.model.ToDO

@Database(entities = [ToDO::class], version = 3)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun getDao(): ToDoDAO

    companion object {
        private var instance: ToDoDatabase? = null

        fun getInstance(context: Context): ToDoDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ToDoDatabase::class.java, "db-todo")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as ToDoDatabase
        }
    }

}