package com.example.organizatudia.features.tasks.data.repository

import android.content.Context
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository
import com.example.organizatudia.features.tasks.data.local.AppDatabase


object TaskRepositoryProvider {

    private lateinit var _taskRepository: TaskRepository

    val taskRepository: TaskRepository
        get() = _taskRepository

    /** Producción (Room) */
    fun init(context: Context) {
        val db = AppDatabase.getInstance(context)
        _taskRepository = RoomTaskRepository(db.taskDao())
    }

    /** Tests / previews */
    fun initInMemory() {
        _taskRepository = InMemoryTaskRepository()
    }
}
