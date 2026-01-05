package com.example.organizatudia.domain.usecase

import com.example.organizatudia.domain.model.Task
import com.example.organizatudia.domain.repository.TaskRepository

class GetTaskByIdUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(id: String): Task? = repo.getTaskById(id)
}
