package com.example.todoappdb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

}