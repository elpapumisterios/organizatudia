package com.example.organizatudia.features.tasks.presentation.createtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar
import com.example.organizatudia.framework.di.LocalAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onBackClick: () -> Unit = {},
    onTaskSaved: () -> Unit = {}
) {


    val container = LocalAppContainer.current
    val vm: CreateTaskViewModel = viewModel(factory = CreateTaskViewModelFactory(container))

    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") } // yyyy-MM-dd
    var startTime by rememberSaveable { mutableStateOf("") } // HH:mm
    var endTime by rememberSaveable { mutableStateOf("") }   // HH:mm
    var category by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val priorities = listOf("Alta", "Media", "Baja")
    var selectedPriority by rememberSaveable { mutableStateOf("Media") }

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            vm.consumeSaved()
            onTaskSaved()
        }
    }

    fun format2(n: Int) = n.toString().padStart(2, '0')

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear nueva tarea") },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("titleField")
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("emailField")
            )

            // Fecha (picker)
            OutlinedTextField(
                value = date,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha límite") },
                placeholder = { Text("yyyy-MM-dd") },
                trailingIcon = {
                    IconButton(onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                date = "${y}-${format2(m + 1)}-${format2(d)}"
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Elegir fecha")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dateField")
            )

            Text("Horario (opcional)", style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Inicio") },
                    placeholder = { Text("HH:mm") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val cal = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, h, min -> startTime = "${format2(h)}:${format2(min)}" },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }) {
                            Icon(Icons.Filled.AccessTime, contentDescription = "Elegir inicio")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("startTimeField")
                )

                OutlinedTextField(
                    value = endTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fin") },
                    placeholder = { Text("HH:mm") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val cal = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, h, min -> endTime = "${format2(h)}:${format2(min)}" },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }) {
                            Icon(Icons.Filled.AccessTime, contentDescription = "Elegir fin")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("endTimeField")
                )
            }

            Text("Prioridad", style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                priorities.forEach { p ->
                    FilterChip(
                        selected = (selectedPriority == p),
                        onClick = { selectedPriority = p },
                        label = { Text(p) }
                    )
                }
            }

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (Personal, Trabajo, ...)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("categoryField")
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("descriptionField"),
                minLines = 4
            )

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("createTaskErrorText")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    vm.saveTask(
                        title = title,
                        email = email,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        category = category,
                        priority = selectedPriority,
                        description = description
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("saveTaskButton"),
                enabled = !uiState.isSaving
            ) {
                Text(if (uiState.isSaving) "GUARDANDO..." else "GUARDAR TAREA")
            }
        }
    }
}
