package com.example.organizatudia.data.repository

import com.example.organizatudia.domain.model.Task
import com.example.organizatudia.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Proveedor central del repositorio de tareas.
 *
 * Toda la app debe usar SIEMPRE TaskRepositoryProvider.taskRepository.
 * Si luego migras a Room o Firebase, solo cambias aquí.
 */
object TaskRepositoryProvider {

    val taskRepository: TaskRepository by lazy {
        InMemoryTaskRepository()
    }
}

/**
 * Implementación en memoria del repositorio.
 *
 * IMPORTANTE:
 * - No tiene tareas por defecto
 * - La lista empieza VACÍA
 * - Solo se llena cuando el usuario crea tareas
 *
 * Este repositorio es ideal para desarrollo rápido.
 */
class InMemoryTaskRepository : TaskRepository {

    // Contenedor único de todas las tareas
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    /** Flujo reactivo con todas las tareas */
    override fun getTasks(): Flow<List<Task>> = tasksFlow.asStateFlow()

    /** Inserta una nueva tarea */
    override suspend fun insertTask(task: Task) {

        // Asegurar que tenga ID único
        val newTask = if (task.id.isBlank()) {
            task.copy(id = UUID.randomUUID().toString())
        } else {
            task
        }

        tasksFlow.update { current ->
            current + newTask
        }
    }

    /** Actualiza una tarea existente */
    override suspend fun updateTask(task: Task) {
        tasksFlow.update { current ->
            current.map { existing ->
                if (existing.id == task.id) task else existing
            }
        }
    }

    /** Busca una tarea por su ID */
    override suspend fun getTaskById(id: String): Task? {
        return tasksFlow.firstOrNull()?.firstOrNull { it.id == id }
    }

    /** Elimina una tarea */
    override suspend fun deleteTask(task: Task) {
        tasksFlow.update { current ->
            current.filterNot { it.id == task.id }
        }
    }
}
