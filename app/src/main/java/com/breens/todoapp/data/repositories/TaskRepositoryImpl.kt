package com.breens.todoapp.data.repositories

import android.util.Log
import com.breens.todoapp.common.COLLECTION_PATH_NAME
import com.breens.todoapp.common.PLEASE_CHECK_INTERNET_CONNECTION
import com.breens.todoapp.common.getCurrentTimeAsString
import com.breens.todoapp.data.model.Task
import com.breens.todoapp.di.IoDispatcher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import com.breens.todoapp.common.Result
import com.breens.todoapp.common.convertDateFormat
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl @Inject constructor(
    private val todoChampDB: FirebaseFirestore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
):TaskRepository  {
    override suspend fun addTask(title: String, body: String): Result<Unit> {
        return try {
            withContext(ioDispatcher){
                val task = hashMapOf(
                    "title" to title,
                    "body" to body,
                    "createdAt" to getCurrentTimeAsString())
                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    todoChampDB.collection(COLLECTION_PATH_NAME)
                        .add(task)
                }
                if (addTaskTimeout == null) {
                    Log.d("ERROR: ", PLEASE_CHECK_INTERNET_CONNECTION)

                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                Result.Success(Unit)
            }
        }catch (exception: Exception){
            Log.d("ERROR: ", "$exception")
            Result.Failure(exception = exception)
        }
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            withContext(ioDispatcher) {
                val fetchingTasksTimeout = withTimeoutOrNull(10000L) {
                    todoChampDB.collection(COLLECTION_PATH_NAME)
                        .get()
                        .await()
                        .documents.map { document ->
                            Task(
                                taskId = document.id,
                                title = document.getString("title") ?: "",
                                body = document.getString("body") ?: "",
                                createdAt = convertDateFormat(
                                    document.getString("createdAt") ?: "",
                                ),
                            )
                        }
                }

                if (fetchingTasksTimeout == null) {
                    Log.d("ERROR: ", PLEASE_CHECK_INTERNET_CONNECTION)

                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Log.d("TASKS: ", "${fetchingTasksTimeout?.toList()}")

                Result.Success(fetchingTasksTimeout?.toList() ?: emptyList())
            }
        } catch (exception: Exception) {
            Log.d("ERROR: ", "$exception")

            Result.Failure(exception = exception)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    todoChampDB.collection(COLLECTION_PATH_NAME)
                        .document(taskId)
                        .delete()
                }

                if (addTaskTimeout == null) {
                    Log.d("ERROR: ", PLEASE_CHECK_INTERNET_CONNECTION)

                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(Unit)
            }
        } catch (exception: Exception) {
            Log.d("ERROR: ", "$exception")

            Result.Failure(exception = exception)
        }
    }

    override suspend fun updateTask(title: String, body: String, taskId: String): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                val taskUpdate: Map<String, Any> = hashMapOf(
                    "title" to title,
                    "body" to body,
                )

                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    todoChampDB.collection(COLLECTION_PATH_NAME)
                        .document(taskId)
                        .update(taskUpdate)
                }

                if (addTaskTimeout == null) {
                    Log.d("ERROR: ", PLEASE_CHECK_INTERNET_CONNECTION)

                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(Unit)
            }
        } catch (exception: Exception) {
            Log.d("ERROR: ", "$exception")

            Result.Failure(exception = exception)
        }
    }
}