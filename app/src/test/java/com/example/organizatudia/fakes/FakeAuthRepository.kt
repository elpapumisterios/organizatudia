package com.example.organizatudia.fakes

import com.example.organizatudia.features.auth.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    var shouldFailLogin = false
    var shouldFailRegister = false
    private var currentEmail: String? = null

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<Unit> {
        return if (shouldFailRegister) {
            Result.failure(IllegalStateException("Register failed"))
        } else {
            currentEmail = email
            Result.success(Unit)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return if (shouldFailLogin) {
            Result.failure(IllegalStateException("Login failed"))
        } else {
            currentEmail = email
            Result.success(Unit)
        }
    }

    override fun logout() {
        currentEmail = null
    }

    override fun currentUserEmail(): String? = currentEmail
}
