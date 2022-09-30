package com.example.todolistchallenge.repository

import androidx.lifecycle.LiveData
import com.example.todolistchallenge.database.models.TodoEntity

interface TodoRepository {

    suspend fun insertTodo(title: String, description: String, createdDate: String, done: Int): Long

    suspend fun updateTodo(id: Long, title: String, description: String, createdDate: String, done: Int)

    suspend fun deleteTodo(id: Long)

    suspend fun deleteAllTodos()

    suspend fun getAllTodos(): List<TodoEntity>

}