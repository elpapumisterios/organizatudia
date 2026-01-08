package com.example.organizatudia.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.features.auth.domain.repository.AuthRepository
import com.example.organizatudia.features.auth.presentation.common.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    fun login(email: String, pass: String) {
        val e = email.trim()
        val p = pass.trim()

        if (e.isBlank() || p.isBlank()) {
            _ui.value = AuthUiState(error = "Completa email y contraseña.")
            return
        }

        _ui.value = AuthUiState(isLoading = true)

        viewModelScope.launch {
            val result = repo.login(e, p)
            _ui.value = result.fold(
                onSuccess = { AuthUiState(success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Error iniciando sesión") }
            )
        }
    }

    fun consumeSuccess() {
        _ui.value = _ui.value.copy(success = false)
    }
}