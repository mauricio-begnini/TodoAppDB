package com.example.todoappdb.ui.viewmodels

import com.example.todoappdb.data.Task

data class TaskScreenUiState(
    val allTasks: List<Task> = listOf()
)
