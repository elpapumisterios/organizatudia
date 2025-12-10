package com.example.organizatudia.presentation.archived

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.organizatudia.data.repository.TaskRepositoryProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedTasksScreen(
    onBackClick: () -> Unit = {}
) {
    val repository = TaskRepositoryProvider.taskRepository
    val allTasks by repository.getTasks().collectAsState(initial = emptyList())
    val archivedTasks = allTasks.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas archivadas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(archivedTasks) { task ->
                ListItem(
                    headlineContent = { Text(task.title) },
                    supportingContent = { Text(task.date) }
                )
                Divider()
            }
        }
    }
}
