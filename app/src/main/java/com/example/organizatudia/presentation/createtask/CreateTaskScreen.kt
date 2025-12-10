package com.example.organizatudia.presentation.createtask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.organizatudia.data.repository.TaskRepositoryProvider
import com.example.organizatudia.domain.model.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onTaskSaved: () -> Unit
) {
    val repository = remember { TaskRepositoryProvider.taskRepository }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }       // por ahora solo informativo
    var deadline by remember { mutableStateOf("") }    // lo usaremos como "fecha"
    var priority by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear nueva tarea") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Fecha límite") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priority,
                onValueChange = { priority = it },
                label = { Text("Prioridad (Alta, Media, Baja)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (Personal, Trabajo, ...)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        scope.launch {
                            val task = Task(
                                title = title,
                                description = description,
                                // por ahora usamos deadline como "date" y "time"
                                date = if (deadline.isNotBlank()) deadline else "Sin fecha",
                                time = "Todo el día",
                                category = if (category.isNotBlank()) category else "General",
                            )
                            repository.insertTask(task)
                            onTaskSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR TAREA")
            }
        }
    }
}
