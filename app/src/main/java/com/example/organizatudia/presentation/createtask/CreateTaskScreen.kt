package com.example.organizatudia.presentation.createtask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen() {
    var title by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Nueva Tarea") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                label = { Text("Fecha Límite") },
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

            Button(onClick = { /* Guardar tarea */ }, modifier = Modifier.fillMaxWidth()) {
                Text("GUARDAR TAREA")
            }
        }
    }
}