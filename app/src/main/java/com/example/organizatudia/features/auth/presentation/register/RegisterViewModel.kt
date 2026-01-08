package com.example.organizatudia.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.features.auth.domain.repository.AuthRepository
import com.example.organizatudia.features.auth.presentation.common.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    fun register(email: String, pass: String, confirm: String) {
        val e = email.trim()
        val p = pass.trim()
        val c = confirm.trim()

        if (e.isBlank() || p.isBlank() || c.isBlank()) {
            _ui.value = AuthUiState(error = "Completa todos los campos")
            return
        }

        if (p != c) {
            _ui.value = AuthUiState(error = "Las contraseñas no coinciden")
            return
        }

        _ui.value = AuthUiState(isLoading = true)

        viewModelScope.launch {
            val result = repo.register(e, p, name = "Usuario")
            _ui.value = result.fold(
                onSuccess = { AuthUiState(success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Error registrando") }
            )
        }
    }

    fun consumeSuccess() {
        _ui.value = _ui.value.copy(success = false)
    }
}