package com.example.organizatudia.domain.usecase

import com.example.organizatudia.domain.repository.TaskRepository

class GetTasksUseCase(private val repo: TaskRepository) {
    operator fun invoke() = repo.getTasks()
}
