package com.example.todolistchallenge.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistchallenge.R
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

    private val _todoUpdateDoneLiveData = MutableLiveData<TodoState>()
    val todoUpdateDoneLiveData: LiveData<TodoState>
        get() = _todoUpdateDoneLiveData

    private val _messageMTLiveData = MutableLiveData<Int>()
    val messageLiveData: LiveData<Int>
        get() = _messageMTLiveData

    fun updateDone(id: Long) = viewModelScope.launch {
        try {
            if(id > 0) {
                repository.updateDone(id)
                _todoUpdateDoneLiveData.value = TodoState.Updated
                _messageMTLiveData.value = R.string.todo_updated_successfully
            }
        } catch (ex: Exception){
            _messageMTLiveData.value = R.string.todo_error_to_update
            Log.e(TAG, ex.toString())

        }
    }


    sealed class TodoState {
        object Updated : TodoState()
    }

    companion object {
        private val TAG = TodoViewModel::class.java.simpleName
    }
}