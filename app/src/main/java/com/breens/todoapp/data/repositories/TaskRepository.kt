package com.breens.todoapp.data.repositories

import com.breens.todoapp.data.model.Task
import com.breens.todoapp.common.Result

interface TaskRepository {
    suspend fun addTask(title: String, body: String): Result<Unit>
    suspend fun getAllTasks(): Result<List<Task>>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun updateTask(title: String, body: String, taskId: String): Result<Unit>
}