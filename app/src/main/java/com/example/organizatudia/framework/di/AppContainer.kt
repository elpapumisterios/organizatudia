package com.example.organizatudia.framework.di

import android.content.Context
import com.example.organizatudia.features.auth.data.FirebaseAuthRepository
import com.example.organizatudia.features.tasks.data.local.AppDatabase
import com.example.organizatudia.features.tasks.data.repository.RoomTaskRepository
import com.example.organizatudia.features.auth.domain.repository.AuthRepository
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository
import com.example.organizatudia.features.tasks.domain.usecase.CreateTaskUseCase
import com.example.organizatudia.features.tasks.domain.usecase.GetTaskByIdUseCase
import com.example.organizatudia.features.tasks.domain.usecase.GetTasksUseCase
import com.example.organizatudia.features.tasks.domain.usecase.UpdateTaskUseCase
import com.example.organizatudia.features.tasks.domain.usecase.DeleteTaskUseCase

//AppContainer es una clase de infraestructura que se encarga de crear y conectar
// las dependencias de la app, como la base de datos, los repositorios y
// los casos de uso.
class AppContainer(context: Context) {

    // ---------- DATA ----------

    //Crea (o reutiliza) la base de datos Room
    private val db = AppDatabase.getInstance(context)

    // Repos (Domain interfaces)
   //
    //TaskRepository	Interfaz del dominio
    //RoomTaskRepository	Implementación real (data)
    //db.taskDao()	Acceso a Room
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
