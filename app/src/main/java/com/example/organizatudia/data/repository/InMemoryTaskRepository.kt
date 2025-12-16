package com.example.organizatudia.data.repository

import android.content.Context
import com.example.organizatudia.data.local.AppDatabase
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
 * Aquí decidimos qué implementación usar (Room en producción).
 */
object TaskRepositoryProvider {

    private lateinit var _taskRepository: TaskRepository

    val taskRepository: TaskRepository
        get() = _taskRepository

    /**
     * Inicializa el repositorio usando Room.
     * Llamar una sola vez, en MainActivity.onCreate().
     */
    fun init(context: Context) {
        if (!::_taskRepository.isInitialized) {
            val db = AppDatabase.getInstance(context)
            _taskRepository = RoomTaskRepository(db.taskDao())
        }
    }

    /**
     * Opción alternativa solo para previews / tests si alguna vez la necesitas.
     */
    fun initInMemory() {
        if (!::_taskRepository.isInitialized) {
            _taskRepository = InMemoryTaskRepository()
        }
    }
}

/**
 * Implementación en memoria del repositorio.
 * La mantenemos por si quieres usarla en pruebas,
 * pero en la app normal ya usamos RoomTaskRepository.
 */
class InMemoryTaskRepository : TaskRepository {

    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    override fun getTasks(): Flow<List<Task>> = tasksFlow.asStateFlow()

    override suspend fun getTaskById(id: String): Task? {
        return tasksFlow.firstOrNull()?.firstOrNull { it.id == id }
    }

    override suspend fun insertTask(task: Task) {
        val newTask = if (task.id.isBlank()) {
            task.copy(id = UUID.randomUUID().toString())
        } else {
            task
        }

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
