package com.example.organizatudia.presentation.auth

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

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    val container = LocalAppContainer.current
    val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(container))

    val ui by vm.ui.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(ui.success) {
        if (ui.success) {
            vm.consumeSuccess()
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().testTag("loginEmailField")
        )

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth().testTag("loginPassField")
        )

        if (ui.error != null) {
            Text(
                text = ui.error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("loginErrorText")
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { vm.login(email, pass) },
            enabled = !ui.isLoading,
            modifier = Modifier.fillMaxWidth().testTag("loginButton")
        ) {
            Text(if (ui.isLoading) "CARGANDO..." else "ENTRAR")
        }

        Button(
            onClick = onGoRegister,
            modifier = Modifier.fillMaxWidth().testTag("goRegisterButton")
        ) {
            Text("Crear cuenta")
        }
    }
}
