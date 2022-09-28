package com.example.todolistchallenge.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolistchallenge.database.models.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(toDo: TodoEntity): Long

    @Update
    suspend fun update(toDo: TodoEntity)

    @Query("DELETE FROM todo_table WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table")
    fun readAllTodos(): LiveData<List<TodoEntity>>
}
