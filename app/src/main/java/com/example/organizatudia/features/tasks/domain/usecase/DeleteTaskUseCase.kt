package com.example.organizatudia.features.tasks.domain.usecase

import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.deleteTask(task)
}
