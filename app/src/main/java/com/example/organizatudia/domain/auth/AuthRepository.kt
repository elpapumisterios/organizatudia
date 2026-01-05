package com.example.organizatudia.domain.auth

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun logout()
    fun currentUserEmail(): String?
}
