package com.example.organizatudia.features.tasks.domain.usecase

import com.example.organizatudia.features.tasks.domain.model.Task
import com.example.organizatudia.features.tasks.domain.model.Achievement

class GetAchievementsUseCase {

    fun calculateAchievements(tasks: List<Task>): List<Achievement> {

        val totalCompleted = tasks.count { it.isCompleted }

        val completedByDay = tasks
            .filter { it.isCompleted }
            .groupBy { it.date } // fecha guardada como String en tu modelo

        return listOf(

            Achievement(
                id = "first",
                title = "Primer paso",
                description = "Completaste tu primera tarea.",
                achieved = totalCompleted >= 1
            ),

            Achievement(
                id = "five_total",
                title = "Arrancando fuerte",
                description = "Has completado 5 tareas.",
                achieved = totalCompleted >= 5
            ),

            Achievement(
                id = "three_one_day",
                title = "Productivo del día",
                description = "Completaste 3 tareas en un mismo día.",
                achieved = completedByDay.values.any { it.size >= 3 }
            ),

            Achievement(
                id = "twenty_total",
                title = "Maestro de la productividad",
                description = "Completaste 20 tareas.",
                achieved = totalCompleted >= 20
            ),

            Achievement(
                id = "five_days",
                title = "Disciplina semanal",
                description = "Completaste tareas en 5 días distintos.",
                achieved = completedByDay.keys.size >= 5
            )
        )
    }
}
