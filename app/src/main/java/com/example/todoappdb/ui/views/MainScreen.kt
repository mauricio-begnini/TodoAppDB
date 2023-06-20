package com.example.todoappdb.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoappdb.R
import com.example.todoappdb.ui.viewmodels.TasksViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    tasksViewModel: TasksViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by tasksViewModel.mainScreenUiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.screenName)},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { tasksViewModel.navigate(navController = navController )}) {
                Icon(
                    painter = painterResource(id = uiState.fabIcon),
                    contentDescription = null
                )
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = "task_list"
        ) {
            composable(route = "task_list") {
                TasksScreen(tasksViewModel = tasksViewModel, navController = navController)
            }
            composable(route = "insert_edit_task") {
                InsertEditTaskScreen(tasksViewModel = tasksViewModel, navController = navController)
            }
        }

    }

}