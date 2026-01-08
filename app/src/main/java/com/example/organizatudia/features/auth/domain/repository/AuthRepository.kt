package com.example.organizatudia.features.auth.domain.repository

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun logout()
    fun currentUserEmail(): String?
}