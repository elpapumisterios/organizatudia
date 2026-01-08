package com.example.organizatudia.features.auth.data

import android.content.Context
import com.example.organizatudia.features.auth.data.FirebaseAuthRepository
import com.example.organizatudia.features.auth.domain.repository.AuthRepository

object AuthRepositoryProvider {

    private lateinit var _authRepository: AuthRepository

    val authRepository: AuthRepository
        get() = _authRepository

    fun init(context: Context) {
        // context no es necesario para FirebaseAuth, pero lo dejamos para consistencia
        _authRepository = FirebaseAuthRepository()
    }

    // Útil si luego quieres tests con fake repo
    fun setForTests(repo: AuthRepository) {
        _authRepository = repo
    }
}