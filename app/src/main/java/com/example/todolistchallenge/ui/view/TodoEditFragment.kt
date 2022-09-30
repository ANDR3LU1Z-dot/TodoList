package com.example.todolistchallenge.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolistchallenge.database.AppDataBase
import com.example.todolistchallenge.database.daos.TodoDao
import com.example.todolistchallenge.databinding.FragmentTodoEditBinding
import com.example.todolistchallenge.repository.DatabaseDataSource
import com.example.todolistchallenge.repository.TodoRepository
import com.example.todolistchallenge.ui.extension.hideKeyboard
import com.example.todolistchallenge.ui.viewmodels.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TodoEditFragment : Fragment() {

//    private lateinit var calendarView:CalendarView

    private lateinit var bindingTodoEditFragment: FragmentTodoEditBinding

    private val viewModel:TodoViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val todoDAO: TodoDao =
                    AppDataBase.getInstance(requireContext()).todoDAO

                val repository: TodoRepository = DatabaseDataSource(todoDAO)
                return TodoViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bindingTodoEditFragment = FragmentTodoEditBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return bindingTodoEditFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvents()
        setListeners()
    }



    private fun observeEvents() {
        viewModel.todoLiveData.observe(viewLifecycleOwner){todoState ->
            when(todoState){
                is TodoViewModel.TodoState.Inserted -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()

                    findNavController().popBackStack()
                }
            }

        }

        viewModel.messageLiveData.observe(viewLifecycleOwner){stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }



    private fun clearFields() {
        bindingTodoEditFragment.inputTitle.text?.clear()
        bindingTodoEditFragment.inputDesc.text?.clear()
    }


    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if(parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {

        bindingTodoEditFragment.button.setOnClickListener {
            val title = bindingTodoEditFragment.inputTitle.text.toString()
            val desc = bindingTodoEditFragment.inputDesc.text.toString()
            val done: Int = when (bindingTodoEditFragment.checkBox.isChecked){
                true -> 1
                else -> 0
            }
            if(title.isEmpty()){
                Toast.makeText(requireContext(), "Preencha o campo Title", Toast.LENGTH_SHORT).show()
            } else{
                viewModel.addTodo(title,desc, getCurrentData(),done)
            }

        }
    }

    private fun getCurrentData(): String {

        val locale = Locale("pt", "BR")

        val localDate = LocalDate.now()

        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate)

    }


}