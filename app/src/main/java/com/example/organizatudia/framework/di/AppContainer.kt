package com.example.organizatudia.framework.di

import android.content.Context
import com.example.organizatudia.data.auth.FirebaseAuthRepository
import com.example.organizatudia.data.local.AppDatabase
import com.example.organizatudia.data.repository.RoomTaskRepository
import com.example.organizatudia.domain.auth.AuthRepository
import com.example.organizatudia.domain.repository.TaskRepository
import com.example.organizatudia.domain.usecase.CreateTaskUseCase
import com.example.organizatudia.domain.usecase.GetTaskByIdUseCase
import com.example.organizatudia.domain.usecase.GetTasksUseCase
import com.example.organizatudia.domain.usecase.UpdateTaskUseCase
import com.example.organizatudia.domain.usecase.DeleteTaskUseCase


class AppContainer(context: Context) {

    // ---------- DATA ----------
    private val db = AppDatabase.getInstance(context)

    // Repos (Domain interfaces)
    val taskRepository: TaskRepository = RoomTaskRepository(taskDao = db.taskDao())
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }

    // ---------- DOMAIN (UseCases) ----------
    val getTasksUseCase by lazy { GetTasksUseCase(repo = taskRepository) }
    val createTaskUseCase by lazy { CreateTaskUseCase(repo = taskRepository) }
    val updateTaskUseCase by lazy { UpdateTaskUseCase(repo = taskRepository) }
    val deleteTaskUseCase by lazy { DeleteTaskUseCase(repo = taskRepository) }
    val getTaskByIdUseCase by lazy { GetTaskByIdUseCase(repo = taskRepository) }

    // Si luego quieres, aquí también podemos meter AuthUseCases (login/register/logout)
}
