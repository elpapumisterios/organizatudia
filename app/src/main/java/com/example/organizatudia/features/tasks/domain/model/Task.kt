package com.example.organizatudia.features.tasks.domain.model

import java.util.UUID

/**
 * Modelo de dominio para una tarea.
 *“Task es el modelo central. Domain trabaja con Task, no con TaskEntity.”
 * date/time siguen siendo String por simplicidad.
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "General",

    // ✅ NUEVO
    val email: String = "",
    val priority: String = "Media", // Alta | Media | Baja

    val isCompleted: Boolean = false,
    val isArchived: Boolean = false
)
