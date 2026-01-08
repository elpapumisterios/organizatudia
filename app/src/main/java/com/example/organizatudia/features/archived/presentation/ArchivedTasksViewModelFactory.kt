package com.example.organizatudia.features.archived.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizatudia.framework.di.AppContainer

class ArchivedTasksViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedTasksViewModel::class.java)) {
            return ArchivedTasksViewModel(
                getTasksUseCase = container.getTasksUseCase,
                getTaskByIdUseCase = container.getTaskByIdUseCase,
                updateTaskUseCase = container.updateTaskUseCase,
                deleteTaskUseCase = container.deleteTaskUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}