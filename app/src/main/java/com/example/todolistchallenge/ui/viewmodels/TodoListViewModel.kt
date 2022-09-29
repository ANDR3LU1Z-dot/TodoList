package com.example.todolistchallenge.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todolistchallenge.repository.TodoRepository

class TodoListViewModel (
    private val repository: TodoRepository
) : ViewModel(){
    val allTodosEvent = repository.getAllTodos()
}