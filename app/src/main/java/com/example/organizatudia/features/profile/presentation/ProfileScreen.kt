package com.example.organizatudia.features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.organizatudia.features.tasks.data.repository.TaskRepositoryProvider

@Composable
fun ProfileScreen() {
    val repository = TaskRepositoryProvider.taskRepository
    val tasks by repository.getTasks().collectAsState(initial = emptyList())

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val completionRate = if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Tarjeta de información básica del usuario (por ahora datos fijos)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Usuario invitado",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Cuenta sin sesión (pronto: Firebase Auth)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Tarjeta de estadísticas de tareas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Estadísticas de tus tareas",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Tareas totales: $totalTasks",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Tareas completadas: $completedTasks",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Porcentaje de completadas: $completionRate%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
