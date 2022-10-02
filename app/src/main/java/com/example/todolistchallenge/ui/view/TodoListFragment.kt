package com.example.todolistchallenge.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistchallenge.R
import com.example.todolistchallenge.database.AppDataBase
import com.example.todolistchallenge.database.daos.TodoDao
import com.example.todolistchallenge.database.models.TodoEntity
import com.example.todolistchallenge.databinding.FragmentTodoListBinding
import com.example.todolistchallenge.repository.DatabaseDataSource
import com.example.todolistchallenge.repository.TodoRepository
import com.example.todolistchallenge.ui.adapters.TodoItemListener
import com.example.todolistchallenge.ui.adapters.TodoListAdapter
import com.example.todolistchallenge.ui.extension.navigateWithAnimations
import com.example.todolistchallenge.ui.viewmodels.TodoListViewModel
import com.example.todolistchallenge.ui.viewmodels.TodoViewModel


class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView

    private val viewModel: TodoListViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val todoDAO: TodoDao =
                    AppDataBase.getInstance(requireContext()).todoDAO

                val repository: TodoRepository = DatabaseDataSource(todoDAO)
                return TodoListViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentTodoListBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        recyclerView = binding.recyclerView
        emptyView = binding.emptyView



        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelEvents()
        configureViewListeners()

//        if(recyclerView.isEmpty()){
//            recyclerView.isGone
//            emptyView.isVisible
//        } else {
//            recyclerView.isVisible
//            emptyView.isGone
//        }

//        val todoListAdapter = TodoListAdapter(
//            listOf(
//                TodoEntity(1, "Estudar Room", "descricao", "00/00/0000",0),
//                TodoEntity(2, "Estudar MVVM", "descricao2", "00/00/0001",1)
//            )
//        )


    }

    private fun observeViewModelEvents() {
        viewModel.allTodosEvent.observe(viewLifecycleOwner) { allTodos ->

            val todoListAdapter = TodoListAdapter(allTodos).apply {
                    onItemClick = { todo ->
                        val directions = TodoListFragmentDirections
                            .actionTodoListFragmentToTodoEditFragment(todo)
                        findNavController().navigateWithAnimations(directions)
                    }
            }

            binding.recyclerView.run {
                setHasFixedSize(true)
                adapter = todoListAdapter
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getTodos()
    }

    private fun configureViewListeners(){
        binding.fabAddTodo.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_todoListFragment_to_todoEditFragment)
        }
    }


}