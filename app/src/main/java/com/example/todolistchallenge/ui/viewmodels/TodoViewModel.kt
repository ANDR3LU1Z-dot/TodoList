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
import java.lang.Exception

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todoMTLiveData = MutableLiveData<TodoState>()
    val todoLiveData: LiveData<TodoState>
        get() = _todoMTLiveData

    private val _messageMTLiveData = MutableLiveData<Int>()
    val messageLiveData: LiveData<Int>
        get() = _messageMTLiveData

    fun addTodo(title: String, description: String, createdDate: String, done: Int) =
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

    sealed class TodoState{
        object Inserted: TodoState()
    }

    companion object {
        private val TAG = TodoViewModel::class.java.simpleName
    }
}