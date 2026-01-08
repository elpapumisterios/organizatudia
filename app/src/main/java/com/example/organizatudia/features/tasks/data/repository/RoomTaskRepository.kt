package com.example.organizatudia.features.tasks.data.repository

import com.example.organizatudia.features.tasks.data.local.TaskDao
import com.example.organizatudia.features.tasks.data.local.toDomain
import com.example.organizatudia.features.tasks.data.local.toEntity
import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * “RoomTaskRepository es la implementación real del
 * repositorio para producción.”
 *
 * Implementación de TaskRepository usando Room.
 */
class RoomTaskRepository(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> =
        taskDao.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getTaskById(id: String): Task? =
        taskDao.getTaskById(id)?.toDomain()

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}
