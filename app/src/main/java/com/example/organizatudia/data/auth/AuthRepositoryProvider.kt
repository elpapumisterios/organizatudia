package com.example.organizatudia.data.auth

import android.content.Context
import com.example.organizatudia.domain.auth.AuthRepository

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
