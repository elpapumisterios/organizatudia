package com.example.organizatudia.presentation.archived

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organizatudia.framework.di.LocalAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedTasksScreen(
    onBackClick: () -> Unit = {}
) {


    val container = LocalAppContainer.current
    val vm: ArchivedTasksViewModel = viewModel(factory = ArchivedTasksViewModelFactory(container))

    val archivedTasks by vm.archivedTasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas archivadas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        if (archivedTasks.isEmpty()) {
            Text(
                text = "No hay tareas archivadas.",
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(archivedTasks, key = { it.id }) { task ->
                Card {
                    ListItem(
                        headlineContent = { Text(task.title) },
                        supportingContent = {
                            Text(
                                listOfNotNull(
                                    task.category.takeIf { it.isNotBlank() },
                                    task.date.takeIf { it.isNotBlank() },
                                    task.time.takeIf { it.isNotBlank() }
                                ).joinToString(" • ")
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { vm.unarchive(task.id) }) {
                                Icon(
                                    imageVector = Icons.Filled.Unarchive,
                                    contentDescription = "Desarchivar"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
