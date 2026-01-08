

package com.example.organizatudia.features.tasks.data.repository

import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.util.UUID

// The TaskRepositoryProvider object has been removed from this file.
// It should be defined in another file within the same package, but only once.

/**
 * Implementación en memoria del repositorio.
 * Útil para pruebas o previews.
 */
class InMemoryTaskRepository : TaskRepository {

    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    override fun getTasks(): Flow<List<Task>> =
        tasksFlow.asStateFlow()

    override suspend fun getTaskById(id: String): Task? {
        return tasksFlow.firstOrNull()
            ?.firstOrNull { it.id == id }
    }

    override suspend fun insertTask(task: Task) {
        val newTask = if (task.id.isBlank()) {
            task.copy(id = UUID.randomUUID().toString())
        } else task

        tasksFlow.update { current ->
            current + newTask
        }
    }

    override suspend fun updateTask(task: Task) {
        tasksFlow.update { current ->
            current.map { existing ->
                if (existing.id == task.id) task else existing
            }
        }
    }

    override suspend fun deleteTask(task: Task) {
        tasksFlow.update { current ->
            current.filterNot { it.id == task.id }
        }
    }
}
