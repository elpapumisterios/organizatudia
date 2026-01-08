package com.example.organizatudia.features.archived.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.usecase.DeleteTaskUseCase
import com.example.organizatudia.features.tasks.domain.usecase.GetTaskByIdUseCase
import com.example.organizatudia.features.tasks.domain.usecase.GetTasksUseCase
import com.example.organizatudia.features.tasks.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArchivedTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _archivedTasks = MutableStateFlow<List<Task>>(emptyList())
    val archivedTasks: StateFlow<List<Task>> = _archivedTasks.asStateFlow()

    init {
        observeArchived()
    }

    private fun observeArchived() {
        viewModelScope.launch {
            getTasksUseCase().collect { tasks ->
                _archivedTasks.value = tasks
                    .filter { it.isArchived }
                    .sortedWith(compareBy<Task> { it.date }.thenBy { it.time }.thenBy { it.title })
            }
        }
    }

    fun unarchive(taskId: String) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId) ?: return@launch
            updateTaskUseCase(task.copy(isArchived = false))
        }
    }

    // Si luego agregas botón, ya tienes listo:
    fun delete(taskId: String) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId) ?: return@launch
            deleteTaskUseCase(task)
        }
    }
}