package com.example.organizatudia.features.tasks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.organizatudia.features.tasks.domain.model.Task
//Modelo “para DB” (cómo se guarda en tabla).
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val category: String,

    //  NUEVO
    val email: String,
    val priority: String,

    val isCompleted: Boolean,
    val isArchived: Boolean
)

fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        date = date,
        time = time,
        category = category,
        email = email,
        priority = priority,
        isCompleted = isCompleted,
        isArchived = isArchived
    )

fun Task.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        description = description,
        date = date,
        time = time,
        category = category,
        email = email,
        priority = priority,
        isCompleted = isCompleted,
        isArchived = isArchived
    )
