package com.example.organizatudia.features.auth.presentation.common

data class AuthUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)