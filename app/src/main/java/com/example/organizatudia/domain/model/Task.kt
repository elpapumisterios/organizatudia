package com.example.organizatudia.domain.model

import java.util.UUID

/**
 * Modelo de dominio para una tarea.
 *
 * En el futuro puedes cambiar date/time a LocalDate/LocalTime si quieres.
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "General",
    val isCompleted: Boolean = false
)
