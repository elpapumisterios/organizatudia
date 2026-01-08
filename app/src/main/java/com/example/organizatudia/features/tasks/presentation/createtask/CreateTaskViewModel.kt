package com.example.organizatudia.features.tasks.presentation.createtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.usecase.CreateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTaskUiState(
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class CreateTaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    fun saveTask(
        title: String,
        email: String,
        date: String,
        startTime: String,
        endTime: String,
        category: String,
        priority: String,
        description: String
    ) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "El título es obligatorio.")
            return
        }

        if (startTime.isNotBlank() && endTime.isNotBlank()) {
            val start = parseHmToMinutes(startTime)
            val end = parseHmToMinutes(endTime)
            if (start != null && end != null && end < start) {
                _uiState.value = _uiState.value.copy(
                    error = "La hora de fin no puede ser menor que la hora de inicio."
                )
                return
            }
        }

        _uiState.value = _uiState.value.copy(isSaving = true, error = null)

        viewModelScope.launch {
            try {
                val timeText = when {
                    startTime.isBlank() && endTime.isBlank() -> "Todo el día"
                    startTime.isNotBlank() && endTime.isBlank() -> startTime.trim()
                    startTime.isBlank() && endTime.isNotBlank() -> endTime.trim()
                    else -> "${startTime.trim()} - ${endTime.trim()}"
                }

                val task = Task(
                    title = title.trim(),
                    email = email.trim(),
                    date = date.trim(),
                    time = timeText,
                    category = category.trim().ifBlank { "General" },
                    priority = priority,
                    description = description.trim()
                )

                createTaskUseCase(task)
                _uiState.value = CreateTaskUiState(saved = true)
            } catch (e: Exception) {
                _uiState.value = CreateTaskUiState(error = e.message ?: "Error al guardar")
            }
        }
    }

    fun consumeSaved() {
        _uiState.value = _uiState.value.copy(saved = false)
    }

    private fun parseHmToMinutes(raw: String): Int? {
        val parts = raw.trim().split(":")
        if (parts.size != 2) return null
        val h = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        if (h !in 0..23 || m !in 0..59) return null
        return h * 60 + m
    }
}
