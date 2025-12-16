package com.example.organizatudia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.organizatudia.domain.model.Task

/**
 * Entidad de Room que representa la tabla de tareas.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val category: String,
    val isCompleted: Boolean
)

/**
 * Mapeos entre capa de datos (Room) y capa de dominio.
 */

fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        date = date,
        time = time,
        category = category,
        isCompleted = isCompleted
    )

fun Task.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        description = description,
        date = date,
        time = time,
        category = category,
        isCompleted = isCompleted
    )
