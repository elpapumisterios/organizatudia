package com.example.organizatudia.features.tasks.data.repository

import android.content.Context
import com.example.organizatudia.features.tasks.data.local.AppDatabase
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository

object TaskRepositoryProvider {

    private lateinit var _taskRepository: TaskRepository

    val taskRepository: TaskRepository
        get() = _taskRepository

    fun init(context: Context) {
        if (!::_taskRepository.isInitialized) {
            val db = AppDatabase.getInstance(context)
            _taskRepository = RoomTaskRepository(taskDao = db.taskDao())
        }
    }
}
