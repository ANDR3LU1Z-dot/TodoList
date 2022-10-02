package com.example.todolistchallenge.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistchallenge.R
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
    private lateinit var inputTitle: EditText
    private lateinit var inputDesc: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button
    private lateinit var checkBox: CheckBox

    private val viewModel: TodoViewModel by viewModels {
        object : ViewModelProvider.Factory {
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

        inputTitle = bindingTodoEditFragment.inputTitle
        inputDesc = bindingTodoEditFragment.inputDesc
        buttonSave = bindingTodoEditFragment.buttonAdd
        buttonDelete = bindingTodoEditFragment.buttonDelete
        checkBox = bindingTodoEditFragment.checkBox

        // Inflate the layout for this fragment
        return bindingTodoEditFragment.root

    }

    private val args: TodoEditFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        args.todo?.let { todo ->
            buttonSave.text = getString(R.string.todo_button_update)
            inputTitle.setText(todo.title)
            inputDesc.setText(todo.description)
            when (todo.done) {
                1 -> checkBox.isChecked = true
                else -> checkBox.isChecked = false
            }
            buttonDelete.visibility = View.VISIBLE
        }
        observeEvents()
        setListeners()
    }


    private fun observeEvents() {
        viewModel.todoLiveData.observe(viewLifecycleOwner) { todoState ->
            when (todoState) {
                is TodoViewModel.TodoState.Inserted,
                is TodoViewModel.TodoState.Updated,
                is TodoViewModel.TodoState.Deleted -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()

                    findNavController().popBackStack()
                }
            }

        }

        viewModel.messageLiveData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }


    private fun clearFields() {
        bindingTodoEditFragment.inputTitle.text?.clear()
        bindingTodoEditFragment.inputDesc.text?.clear()
    }


    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {

        bindingTodoEditFragment.buttonAdd.setOnClickListener {
            val title = bindingTodoEditFragment.inputTitle.text.toString()
            val desc = bindingTodoEditFragment.inputDesc.text.toString()
            val done: Int = when (bindingTodoEditFragment.checkBox.isChecked) {
                true -> 1
                else -> 0
            }
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha o campo Title", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.insertOrUpdateTodo(
                    title,
                    desc,
                    getCurrentData(),
                    done,
                    args.todo?.id ?: 0
                )
            }

        }

        buttonDelete.setOnClickListener {
            viewModel.removeTodo(args.todo?.id ?: 0)
        }
    }

    private fun getCurrentData(): String {

        val locale = Locale("pt", "BR")

        val localDate = LocalDate.now()

        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate)

    }


}