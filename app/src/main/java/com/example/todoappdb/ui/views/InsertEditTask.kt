package com.example.todoappdb.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.todoappdb.ui.viewmodels.TasksViewModel

@Composable
fun InsertEditTaskScreen(
    tasksViewModel: TasksViewModel,
    navController: NavController,
) {
    BackHandler() {
        tasksViewModel.navigateBack(navController)
    }
    val uiState by tasksViewModel.insertEditScreenUiState.collectAsState()
    InsertEditForm(
        name = uiState.taskName,
        isImportant = uiState.isImportant,
        onNameChanged = tasksViewModel::onTaskNameChange,
        onIsImportantChanged = tasksViewModel::onTaskImportanceChange
    )
}

@Composable
fun InsertEditForm(
    name: String,
    isImportant: Boolean,
    onNameChanged: (String) -> Unit,
    onIsImportantChanged: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = { Text(text = "Task Name") },
                value = name,
                onValueChange = onNameChanged
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isImportant, onCheckedChange = onIsImportantChanged)
                Text(text = "Important Task")
            }
        }
    }
}