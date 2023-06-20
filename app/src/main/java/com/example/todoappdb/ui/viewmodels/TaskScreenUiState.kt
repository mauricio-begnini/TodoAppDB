package com.example.todoappdb.ui.viewmodels

import com.example.todoappdb.data.Task

data class TaskScreenUiState(
    val allTasks: List<Task> = listOf(
        Task("Preparar aula de PDM", isImportant = true),
        Task("Gravar aula de PDM",),
        Task("Corrigir provas de AOC", isImportant = true, isCompleted = true),
        Task("Elaborar projeto de AOC"),
    )
)
