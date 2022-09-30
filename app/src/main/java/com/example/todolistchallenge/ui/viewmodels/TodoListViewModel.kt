package com.example.todolistchallenge.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistchallenge.database.models.TodoEntity
import com.example.todolistchallenge.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoListViewModel (
    private val repository: TodoRepository
) : ViewModel(){

    private val _allTodosMTEvent = MutableLiveData<List<TodoEntity>>()
    val allTodosEvent: LiveData<List<TodoEntity>>
        get() = _allTodosMTEvent


    fun getTodos() = viewModelScope.launch {
        _allTodosMTEvent.postValue(repository.getAllTodos())
    }
}