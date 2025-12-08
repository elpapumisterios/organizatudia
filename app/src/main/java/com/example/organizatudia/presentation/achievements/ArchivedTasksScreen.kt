package com.example.organizatudia.presentation.archived

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedTasksScreen() {
    val archivedTasks = listOf(
        "Tarea archivada 1",
        "Tarea archivada 2",
        "Tarea archivada 3"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas archivadas") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: acción si quieres */ }) {
                        Icon(
                            imageVector = Icons.Filled.Archive,
                            contentDescription = "Tareas archivadas"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(archivedTasks) { task ->
                ListItem(
                    headlineContent = { Text(task) }
                )
                Divider()
            }
        }
    }
}
