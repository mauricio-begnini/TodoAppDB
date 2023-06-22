package com.example.todoappdb.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoappdb.R
import com.example.todoappdb.data.Task
import com.example.todoappdb.ui.viewmodels.TasksViewModel

@Composable
fun TasksScreen(
    tasksViewModel: TasksViewModel,
    navController: NavController,
) {

    val uiState by tasksViewModel.taskScreenUiState.collectAsState()
    taskList(
        tasks = uiState.allTasks,
        onCompletedChange = tasksViewModel::onTaskIsCompletedChange,
        onEditTask = {tasksViewModel.editTask(task = it, navController = navController)},
    )
}

@Composable
fun taskList(
    tasks: List<Task>,
    onCompletedChange: (Task, Boolean) -> Unit,
    onEditTask: (Task) -> Unit,
) {
    LazyColumn() {
        items(tasks) { task ->
            TaskEntry(
                task = task,
                onCompletedChange = {onCompletedChange(task, it)},
                onEditTask = {onEditTask(task)},
            )
        }
    }
}

@Composable
fun TaskEntry(
    task: Task,
    onCompletedChange: (Boolean) -> Unit,
    onEditTask: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onEditTask() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                if (task.isImportant) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_priority_high_24),
                        contentDescription = "high priority"
                    )
                }
                Text(text = task.name)
            }
            Checkbox(checked = task.isCompleted, onCheckedChange = onCompletedChange)
        }
    }
}