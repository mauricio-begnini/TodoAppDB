package com.example.todoappdb.ui.viewmodels

import androidx.annotation.DrawableRes
import com.example.todoappdb.R

data class MainScreenUiState(
    val screenName: String = "Task List",
    @DrawableRes val fabIcon: Int = R.drawable.baseline_add_24,
)
