package com.example.organizatudia.features.tasks.domain.usecase

import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository

class GetTaskByIdUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(id: String): Task? = repo.getTaskById(id)
}
