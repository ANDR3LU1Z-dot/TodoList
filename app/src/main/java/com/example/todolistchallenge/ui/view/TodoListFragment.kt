package com.example.todolistchallenge.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolistchallenge.R
import com.example.todolistchallenge.database.models.TodoEntity
import com.example.todolistchallenge.databinding.FragmentTodoListBinding
import com.example.todolistchallenge.ui.adapters.TodoListAdapter


class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentTodoListBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoListAdapter = TodoListAdapter(
            listOf(
                TodoEntity(1, "Estudar Room", "descricao", "00/00/0000",0),
                TodoEntity(2, "Estudar MVVM", "descricao2", "00/00/0001",1)
            )
        )

        binding.recyclerView.run {
            setHasFixedSize(true)
            adapter = todoListAdapter
        }
    }


}