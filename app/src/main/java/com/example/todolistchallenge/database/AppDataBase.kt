package com.example.todolistchallenge.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolistchallenge.database.daos.TodoDao
import com.example.todolistchallenge.database.models.TodoEntity

@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase(){

    abstract val todoDAO: TodoDao

    companion object{

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase{
            synchronized(this){
                var instance: AppDataBase? = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDataBase::class.java,
                        "app_database"
                    ).build()
                }
                return instance
            }
        }
    }


}
