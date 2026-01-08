package com.example.organizatudia.features.tasks.domain.usecase

import com.example.organizatudia.features.tasks.domain.repository.TaskRepository

class GetTasksUseCase(private val repo: TaskRepository) {
    operator fun invoke() = repo.getTasks()
}
