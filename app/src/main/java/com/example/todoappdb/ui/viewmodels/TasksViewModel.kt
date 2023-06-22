package com.example.todoappdb.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.todoappdb.R
import com.example.todoappdb.TaskApplication
import com.example.todoappdb.data.Task
import com.example.todoappdb.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val taskDao: TaskDao,
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private var _mainScreenUiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(
        MainScreenUiState()
    )
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState.asStateFlow()

    val taskScreenUiState: StateFlow<TaskScreenUiState> =
        taskDao.getAllTasks().map { TaskScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TaskScreenUiState()
            )

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
        val task = updatedTask.copy(isCompleted = newTaskCompletion)
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun navigate(navController: NavController) {
        if (mainScreenUiState.value.screenName == "Task List")
            navigateToInsertEditTask(navController = navController)
        else
            navigateToTaskList(navController = navController)
    }

    private fun navigateToInsertEditTask(navController: NavController) {
        if (editTask) {
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
        } else {
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
            val task = taskToEdit.copy(
                name = insertEditScreenUiState.value.taskName,
                isImportant = insertEditScreenUiState.value.isImportant
            )
            viewModelScope.launch {
                taskDao.update(task)
            }
            editTask = false
            taskToEdit = Task()
        } else {
            val task = Task(
                name = insertEditScreenUiState.value.taskName,
                isImportant = insertEditScreenUiState.value.isImportant,
            )
            viewModelScope.launch {
                taskDao.insert(task)
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
        navController.navigate("task_list") {
            popUpTo("task_list") {
                inclusive = true
            }
        }
    }

    fun editTask(task: Task, navController: NavController) {
        editTask = true
        taskToEdit = task
        navigateToInsertEditTask(navController)
    }

    fun navigateBack(navController: NavController) {
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val saveStateHandler = extras.createSavedStateHandle()
                return TasksViewModel(
                    (application as TaskApplication).database.taskDao(),
                    saveStateHandler
                ) as T
            }
        }
    }

}