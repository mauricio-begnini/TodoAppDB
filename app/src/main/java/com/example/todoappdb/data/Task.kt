package com.example.todoappdb.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    @ColumnInfo(name = "is_important") val isImportant: Boolean = false,
    @ColumnInfo(name = "is_completed")val isCompleted: Boolean = false,
)
