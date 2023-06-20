package com.example.todoappdb.data

data class Task(
    val name: String = "",
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
)
