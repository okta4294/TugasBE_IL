package com.breens.todoapp.feature_tasks.ui.side_effects

sealed class TaskScreenSideEffects {
    data class ShowSnackBarMessage(val message: String) : TaskScreenSideEffects()
}