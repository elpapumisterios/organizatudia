package com.example.organizatudia.features.tasks.domain.usecase

//CreateTaskUseCase (regla de negocio)
import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository

class CreateTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.insertTask(task)
}
