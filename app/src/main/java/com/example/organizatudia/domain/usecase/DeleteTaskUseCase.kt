package com.example.organizatudia.domain.usecase

import com.example.organizatudia.domain.model.Task
import com.example.organizatudia.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.deleteTask(task)
}
