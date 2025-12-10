package com.example.organizatudia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.data.repository.TaskRepositoryProvider
import com.example.organizatudia.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiModel(
    val id: String,
    val title: String,
    val time: String,
    val isCompleted: Boolean = false
)

data class HomeUiState(
    val tasks: List<TaskUiModel> = emptyList(),
    val progress: Float = 0f,
    val progressText: String = "0/100%",
    val level: String = "Empezando"
)

class HomeViewModel : ViewModel() {

    private val repository = TaskRepositoryProvider.taskRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    /**
     * Escucha continuamente la lista de tareas desde el repositorio
     * y actualiza el estado de la UI (lista + barra + nivel).
     */
    private fun observeTasks() {
        viewModelScope.launch {
            repository.getTasks().collect { tasks ->
                val uiTasks = tasks.map { it.toUiModel() }

                val progressInfo = calculateProgress(uiTasks)

                _uiState.update { current ->
                    current.copy(
                        tasks = uiTasks,
                        progress = progressInfo.progress,
                        progressText = progressInfo.progressText,
                        level = progressInfo.level
                    )
                }
            }
        }
    }

    /**
     * Cuando el usuario marca/desmarca una tarea, actualizamos
     * el repositorio. El flujo se encargará de refrescar la UI.
     */
    fun onTaskCheckedChanged(taskId: String, isChecked: Boolean) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId) ?: return@launch
            repository.updateTask(task.copy(isCompleted = isChecked))
        }
    }

    // --------- Lógica de progreso ---------

    private data class ProgressInfo(
        val progress: Float,
        val progressText: String,
        val level: String
    )

    private fun calculateProgress(tasks: List<TaskUiModel>): ProgressInfo {
        val totalTasks = tasks.size
        if (totalTasks == 0) {
            return ProgressInfo(
                progress = 0f,
                progressText = "0/100%",
                level = "Empezando"
            )
        }

        val completedTasks = tasks.count { it.isCompleted }
        val progressPercentage = completedTasks.toFloat() / totalTasks.toFloat()
        val progressText = "${(progressPercentage * 100).toInt()}/100%"

        val level = when ((progressPercentage * 100).toInt()) {
            in 0..33 -> "Empezando"
            in 34..66 -> "Brillante"
            else -> "Extraordinario"
        }

        return ProgressInfo(
            progress = progressPercentage,
            progressText = progressText,
            level = level
        )
    }

    // --------- Mapeo dominio -> UI ---------

    private fun Task.toUiModel(): TaskUiModel =
        TaskUiModel(
            id = id,
            title = title,
            time = time,
            isCompleted = isCompleted
        )
}
