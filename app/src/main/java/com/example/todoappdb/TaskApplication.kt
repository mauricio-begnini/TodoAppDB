package com.example.todoappdb

import android.app.Application
import com.example.todoappdb.data.TaskRoomDatabase

class TaskApplication : Application() {

    val database: TaskRoomDatabase by lazy {
        TaskRoomDatabase.getDatabase(this)
    }

}