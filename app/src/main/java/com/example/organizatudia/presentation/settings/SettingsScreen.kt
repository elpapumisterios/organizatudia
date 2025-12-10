package com.example.organizatudia.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onProfileClick: () -> Unit = {},
    onTasksClick: () -> Unit = {},
    onTreeProgressClick: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onArchivedTasksClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingsItem(
            icon = Icons.Default.AccountCircle,
            text = "Mi perfil",
            onClick = onProfileClick
        )

        SettingsItem(
            icon = Icons.AutoMirrored.Filled.List,
            text = "Mis tareas",
            onClick = onTasksClick
        )

        SettingsItem(
            icon = Icons.Default.Star,
            text = "Árbol progreso",
            onClick = onTreeProgressClick
        )

        SettingsItem(
            icon = Icons.Default.Star,
            text = "Logros",
            onClick = onAchievementsClick
        )

        SettingsItem(
            icon = Icons.Default.Notifications,
            text = "Notificaciones",
            onClick = onNotificationsClick
        )

        SettingsItem(
            icon = Icons.Default.Archive,
            text = "Tareas archivadas",
            onClick = onArchivedTasksClick
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Divider()
}
