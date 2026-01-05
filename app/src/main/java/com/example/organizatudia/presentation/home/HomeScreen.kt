package com.example.organizatudia.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organizatudia.R
import androidx.compose.runtime.*
import com.example.organizatudia.framework.di.LocalAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddTaskClick: () -> Unit
) {


    val container = LocalAppContainer.current
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(container))

    val uiState by homeViewModel.uiState.collectAsState()

    var showOverdue by rememberSaveable { mutableStateOf(false) }

    val plantRes = when (uiState.level) {
        "Brillante" -> R.drawable.plant_stage_2
        "Extraordinario" -> R.drawable.plant_stage_3
        else -> R.drawable.plant_stage_1
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = plantRes),
                contentDescription = "Planta - ${uiState.level}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Nivel: ${uiState.level}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = uiState.progressText, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${uiState.doneToday} de ${uiState.totalToday} tareas completadas hoy",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Empezando", style = MaterialTheme.typography.labelSmall)
                Text("Brillante", style = MaterialTheme.typography.labelSmall)
                Text("Extraordinario", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Mis Tareas", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.tasks.isEmpty() && uiState.overdueTasks.isEmpty()) {
                Text("No hay tareas.")
            } else {

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    items(uiState.tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { checked ->
                                homeViewModel.onTaskCheckedChanged(task.id, checked)
                            },
                            onArchiveClick = { homeViewModel.onArchiveTask(task.id) }
                        )
                    }

                    if (uiState.overdueTasks.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showOverdue = !showOverdue }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "No completadas (${uiState.overdueTasks.size})",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Icon(
                                        imageVector = if (showOverdue) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Expandir"
                                    )
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(visible = showOverdue) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    uiState.overdueTasks.forEach { task ->
                                        OverdueTaskItem(
                                            task = task,
                                            onCheckedChange = { checked ->
                                                homeViewModel.onTaskCheckedChanged(task.id, checked)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskUiModel,
    onCheckedChange: (Boolean) -> Unit,
    onArchiveClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)

                val info = listOfNotNull(
                    task.time.takeIf { it.isNotBlank() },
                    task.category.takeIf { it.isNotBlank() },
                    task.date.takeIf { it.isNotBlank() }
                ).joinToString(" • ")

                if (info.isNotBlank()) {
                    Text(
                        text = info,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (task.isCompleted) {
                    IconButton(onClick = onArchiveClick) {
                        Icon(imageVector = Icons.Default.Archive, contentDescription = "Archivar")
                    }
                }
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckedChange
                )
            }
        }
    }
}

@Composable
private fun OverdueTaskItem(
    task: TaskUiModel,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Vencida • ${task.date} • ${task.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
