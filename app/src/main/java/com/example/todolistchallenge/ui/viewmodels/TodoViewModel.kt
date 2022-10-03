package com.example.todolistchallenge.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistchallenge.R
import com.example.todolistchallenge.repository.TodoRepository
import kotlinx.coroutines.launch
import kotlin.Exception

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todoMTLiveData = MutableLiveData<TodoState>()
    val todoLiveData: LiveData<TodoState>
        get() = _todoMTLiveData

    private val _messageMTLiveData = MutableLiveData<Int>()
    val messageLiveData: LiveData<Int>
        get() = _messageMTLiveData

    fun insertOrUpdateTodo(
        title: String,
        description: String,
        createdDate: String,
        done: Int,
        id: Long = 0
    ) {
        if (id > 0) {
            updateTodo(id, title, description, createdDate, done)
        } else {
            insertTodo(title, description, createdDate, done)
        }
    }

    private fun updateTodo(id: Long, title: String, description: String,createdDate: String, done: Int) =
        viewModelScope.launch {
            try{
                repository.updateTodo(id, title, description, createdDate, done )

                _todoMTLiveData.value = TodoState.Updated
                _messageMTLiveData.value = R.string.todo_updated_successfully
            } catch (ex: Exception){
                _messageMTLiveData.value = R.string.todo_error_to_update
                Log.e(TAG, ex.toString())
            }
        }

    fun insertTodo(title: String, description: String, createdDate: String, done: Int) =
        viewModelScope.launch {
            try {
                val id = repository.insertTodo(title, description, createdDate, done)
                if (id > 0) {
                    _todoMTLiveData.value = TodoState.Inserted
                    _messageMTLiveData.value = R.string.todo_inserted_successfully
                }
            } catch (ex: Exception) {
                _messageMTLiveData.value = R.string.todo_error_to_insert
                Log.e(TAG, ex.toString())
            }
        }

    fun removeTodo(id: Long) = viewModelScope.launch {
        try{
            if (id > 0){
                repository.deleteTodo(id)
                _todoMTLiveData.value = TodoState.Deleted
                _messageMTLiveData.value = R.string.todo_deleted_sucessfully
            }
        } catch (ex: Exception){
            _messageMTLiveData.value = R.string.todo_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    sealed class TodoState {
        object Inserted : TodoState()
        object Updated : TodoState()
        object Deleted: TodoState()
    }

    companion object {
        private val TAG = TodoViewModel::class.java.simpleName
    }
}