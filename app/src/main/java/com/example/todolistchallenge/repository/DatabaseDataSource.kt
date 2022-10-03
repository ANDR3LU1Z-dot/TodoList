package com.example.todolistchallenge.repository

import androidx.lifecycle.LiveData
import com.example.todolistchallenge.database.daos.TodoDao
import com.example.todolistchallenge.database.models.TodoEntity

class DatabaseDataSource(private val todoDAO: TodoDao) : TodoRepository {
    override suspend fun insertTodo(
        title: String,
        description: String,
        createdDate: String,
        done: Int
    ): Long {
        val todo = TodoEntity(
            title = title,
            description = description,
            createdDate = createdDate,
            done = done
        )

       return todoDAO.insert(todo)
    }

    override suspend fun updateTodo(
        id: Long,
        title: String,
        description: String,
        createdDate: String,
        done: Int
    ) {
        val todo = TodoEntity(
            id = id,
            title = title,
            description = description,
            createdDate = createdDate,
            done = done
        )
        todoDAO.update(todo)
    }

    override suspend fun updateDone(id: Long) {

        todoDAO.updateDone(id)
    }

    override suspend fun deleteTodo(id: Long) {
        todoDAO.delete(id)
    }

    override suspend fun deleteAllTodos() {
        todoDAO.deleteAll()
    }

    override suspend fun getAllTodos(): List<TodoEntity> {
        return todoDAO.readAllTodos()
    }
}