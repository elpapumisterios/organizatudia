package com.example.organizatudia.presentation.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.organizatudia.data.repository.TaskRepositoryProvider
import com.example.organizatudia.domain.model.Achievement
import com.example.organizatudia.domain.model.Task

@Composable
fun AchievementsScreen() {
    val repository = TaskRepositoryProvider.taskRepository
    val tasks by repository.getTasks().collectAsState(initial = emptyList())

    val achievements = calculateAchievements(tasks)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "LOGROS",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(achievements) { achievement ->
                AchievementItem(achievement)
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: Achievement) {
    val icon = if (achievement.achieved) Icons.Filled.CheckCircle else Icons.Filled.Lock
    val containerColor =
        if (achievement.achieved) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Lógica de logros basada en las tareas COMPLETADAS.
 */
private fun calculateAchievements(tasks: List<Task>): List<Achievement> {
    val completed = tasks.filter { it.isCompleted }
    val totalCompleted = completed.size

    val completedByDay = completed.groupBy { it.date }
    val distinctCategories = completed.map { it.category }.toSet()

    return listOf(
        Achievement(
            id = "first_task",
            title = "Primera tarea completada",
            description = "Marca al menos una tarea como completada.",
            achieved = totalCompleted >= 1
        ),
        Achievement(
            id = "five_days",
            title = "5 días consecutivos",
            description = "Completa tareas en al menos 5 días distintos.",
            achieved = completedByDay.keys.size >= 5
        ),
        Achievement(
            id = "ten_one_day",
            title = "10 tareas en un día",
            description = "Completa 10 tareas en un mismo día.",
            achieved = completedByDay.values.any { it.size >= 10 }
        ),
        Achievement(
            id = "fifty_total",
            title = "Completar 50 tareas",
            description = "Llega a 50 tareas completadas en total.",
            achieved = totalCompleted >= 50
        ),
        Achievement(
            id = "all_categories",
            title = "Usar todas las categorías",
            description = "Completa tareas usando varias categorías diferentes.",
            achieved = distinctCategories.size >= 3
        ),
        Achievement(
            id = "master_level",
            title = "Nivel maestro alcanzado",
            description = "Completa 100 tareas o más.",
            achieved = totalCompleted >= 100
        )
    )
}
