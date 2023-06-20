package com.example.todoappdb.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.todoappdb.R
import com.example.todoappdb.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TasksViewModel : ViewModel() {

    private var _mainScreenUiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(
        MainScreenUiState()
    )
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState.asStateFlow()

    private var _taskScreenUiState: MutableStateFlow<TaskScreenUiState> = MutableStateFlow(
        TaskScreenUiState()
    )
    val taskScreenUiState: StateFlow<TaskScreenUiState> = _taskScreenUiState.asStateFlow()

    private var _insertEditUiState: MutableStateFlow<InsertEditScreenUiState> = MutableStateFlow(
        InsertEditScreenUiState()
    )
    val insertEditScreenUiState: StateFlow<InsertEditScreenUiState> =
        _insertEditUiState.asStateFlow()

    private var editTask: Boolean = false
    private var taskToEdit: Task = Task()

    fun onTaskNameChange(newTaskName: String) {
        _insertEditUiState.update { currentState ->
            currentState.copy(taskName = newTaskName)
        }
    }

    fun onTaskImportanceChange(newTaskImportance: Boolean) {
        _insertEditUiState.update { currentState ->
            currentState.copy(isImportant = newTaskImportance)
        }
    }

    fun onTaskIsCompletedChange(updatedTask: Task, newTaskCompletion: Boolean) {
        val allTasksTemp = _taskScreenUiState.value.allTasks.toMutableList()
        var taskPos = -1
        allTasksTemp.forEachIndexed { index, task ->
            if (task == updatedTask) {
                taskPos = index
            }
        }
        allTasksTemp.removeAt(index = taskPos)
        allTasksTemp.add(
            index = taskPos,
            element = updatedTask.copy(isCompleted = newTaskCompletion)
        )
        _taskScreenUiState.update { currentState ->
            currentState.copy(allTasks = allTasksTemp.toList())
        }
    }

    fun navigate(navController: NavController) {
        if (mainScreenUiState.value.screenName == "Task List")
            navigateToInsertEditTask(navController = navController)
        else
            navigateToTaskList(navController = navController)
    }

    private fun navigateToInsertEditTask(navController: NavController) {
        if(editTask){
            _mainScreenUiState.update { currentState ->
                currentState.copy(
                    screenName = "Edit Task",
                    fabIcon = R.drawable.baseline_check_24,
                )
            }
            _insertEditUiState.update { currentState ->
                currentState.copy(
                    taskName = taskToEdit.name,
                    isImportant = taskToEdit.isImportant,
                )
            }
        }else{
            _mainScreenUiState.update { currentState ->
                currentState.copy(
                    screenName = "Create New Task",
                    fabIcon = R.drawable.baseline_check_24,
                )
            }
        }
        navController.navigate("insert_edit_task")
    }

    private fun navigateToTaskList(navController: NavController) {
        if (editTask) {
            val allTasksTemp = _taskScreenUiState.value.allTasks.toMutableList()
            var taskPos = -1
            allTasksTemp.forEachIndexed { index, task ->
                if (task == taskToEdit) {
                    taskPos = index
                }
            }
            allTasksTemp.removeAt(index = taskPos)
            allTasksTemp.add(
                index = taskPos,
                element = taskToEdit.copy(
                    name = insertEditScreenUiState.value.taskName,
                    isImportant = insertEditScreenUiState.value.isImportant
                )
            )
            _taskScreenUiState.update { currentState ->
                currentState.copy(allTasks = allTasksTemp.toList())
            }
            editTask = false
            taskToEdit = Task()
        } else {
            _taskScreenUiState.update { currentState ->
                currentState.copy(
                    allTasks = currentState.allTasks + Task(
                        name = insertEditScreenUiState.value.taskName,
                        isImportant = insertEditScreenUiState.value.isImportant,
                    )
                )
            }
        }
        _insertEditUiState.update { currentState ->
            currentState.copy(
                taskName = "",
                isImportant = false,
            )
        }
        _mainScreenUiState.update { currentState ->
            currentState.copy(screenName = "Task List", fabIcon = R.drawable.baseline_add_24)
        }
        navController.navigate("task_list"){
            popUpTo("task_list"){
                inclusive = true
            }
        }
    }

    fun editTask(task: Task, navController: NavController){
        editTask = true
        taskToEdit = task
        navigateToInsertEditTask(navController)
    }

    fun navigateBack(navController: NavController){
        editTask = false
        taskToEdit = Task()
        _insertEditUiState.update { currentState ->
            currentState.copy(
                taskName = "",
                isImportant = false,
            )
        }
        _mainScreenUiState.update { currentState ->
            currentState.copy(screenName = "Task List", fabIcon = R.drawable.baseline_add_24)
        }
        navController.popBackStack()
    }
}