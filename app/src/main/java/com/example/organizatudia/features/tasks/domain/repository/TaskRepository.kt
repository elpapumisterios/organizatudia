package com.example.organizatudia.features.tasks.domain.repository

import com.example.organizatudia.features.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de tareas.
 * “TaskRepository es la interfaz de dominio: define qué operaciones existen,
 * sin decir cómo se guardan.”
 */
///TaskRepository (contrato / interfaz)
interface TaskRepository {

    /** Flujo reactivo con la lista de tareas. */
    fun getTasks(): Flow<List<Task>>

    /** Obtiene una tarea por id, o null si no existe. */
    suspend fun getTaskById(id: String): Task?

    /** Inserta una nueva tarea. */
    suspend fun insertTask(task: Task)

    /** Actualiza una tarea existente. */
    suspend fun updateTask(task: Task)

    /** Elimina una tarea. */
    suspend fun deleteTask(task: Task)
}
