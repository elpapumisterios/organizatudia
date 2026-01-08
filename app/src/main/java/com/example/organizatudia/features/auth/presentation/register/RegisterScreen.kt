package com.example.organizatudia.features.auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organizatudia.framework.di.LocalAppContainer
import com.example.organizatudia.features.auth.presentation.register.RegisterViewModel
import com.example.organizatudia.features.auth.presentation.register.RegisterViewModelFactory

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val container = LocalAppContainer.current
    val vm: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(container))

    val ui by vm.ui.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(ui.success) {
        if (ui.success) {
            vm.consumeSuccess()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registro", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerEmailField")
        )

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerPassField")
        )

        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerConfirmField")
        )

        if (ui.error != null) {
            Text(
                text = ui.error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("registerErrorText")
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { vm.register(email, pass, confirm) },
            enabled = !ui.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerButton")
        ) {
            Text(if (ui.isLoading) "CARGANDO..." else "REGISTRAR")
        }

        Button(
            onClick = onBackToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("backToLoginButton")
        ) {
            Text("Volver a login")
        }
    }
}
