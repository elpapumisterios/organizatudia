package com.example.organizatudia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiModel(
    val id: Int,
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

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDummyTasks()
    }

    private fun loadDummyTasks() {
        val dummyTasks = listOf(
            TaskUiModel(1, "Hacer ejercicio", "08:00 AM", false),
            TaskUiModel(2, "Revisar correos", "09:00 AM", false),
            TaskUiModel(3, "Reunión de equipo", "11:00 AM", true),
            TaskUiModel(4, "Planificar el día siguiente", "08:00 PM", false)
        )
        _uiState.update { it.copy(tasks = dummyTasks) }
        recalculateProgress()
    }

    fun onTaskCheckedChanged(taskId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedTasks = currentState.tasks.map { task ->
                    if (task.id == taskId) {
                        task.copy(isCompleted = isChecked)
                    } else {
                        task
                    }
                }
                currentState.copy(tasks = updatedTasks)
            }
            recalculateProgress()
        }
    }

    private fun recalculateProgress() {
        _uiState.update { currentState ->
            val totalTasks = currentState.tasks.size
            if (totalTasks == 0) return@update currentState

            val completedTasks = currentState.tasks.count { it.isCompleted }
            val progressPercentage = (completedTasks.toFloat() / totalTasks.toFloat())
            val progressText = "${(progressPercentage * 100).toInt()}/100%"

            val level = when ((progressPercentage * 100).toInt()) {
                in 0..33 -> "Empezando"
                in 34..66 -> "Brillante"
                else -> "Extraordinario"
            }

            currentState.copy(
                progress = progressPercentage,
                progressText = progressText,
                level = level
            )
        }
    }
}