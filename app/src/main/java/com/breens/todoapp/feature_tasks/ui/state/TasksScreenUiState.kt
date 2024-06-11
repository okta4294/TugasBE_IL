package com.breens.todoapp.feature_tasks.ui.state

import com.breens.todoapp.data.model.Task

data class TasksScreenUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,
    val taskToBeUpdated: Task? = null,
    val isShowAddTaskDialog: Boolean = false,
    val isShowUpdateTaskDialog: Boolean = false,
    val currentTextFieldTitle: String = "",
    val currentTextFieldBody: String = "",)