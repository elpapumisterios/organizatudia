package com.example.organizatudia.presentation.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Achievement(val name: String, val completed: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen() {
    val achievements = listOf(
        Achievement("Primera tarea completada", true),
        Achievement("5 días consecutivos", true),
        Achievement("10 tareas en un día", false),
        Achievement("Completar 50 tareas", false),
        Achievement("Usar todas las categorías", true),
        Achievement("Nivel maestro alcanzado", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("LOGROS") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementItem(achievement = achievement)
            }
        }
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (achievement.completed) Color(0xFF4CAF50) else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                if (achievement.completed) {
                    Icon(Icons.Default.Done, contentDescription = "Completado", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(achievement.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}