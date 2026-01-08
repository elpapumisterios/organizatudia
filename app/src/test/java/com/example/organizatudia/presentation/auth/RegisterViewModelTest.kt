package com.example.organizatudia.presentation.auth

import com.example.organizatudia.MainDispatcherRule
import com.example.organizatudia.fakes.FakeAuthRepository
import com.example.organizatudia.features.auth.presentation.register.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
//Registro válido
//
//Registro con campos vacíos
//
//Validaciones de dominio
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `si passwords no coinciden muestra error`() = runTest {
        val repo = FakeAuthRepository()
        val vm = RegisterViewModel(repo)

        vm.register(
            email = "a@a.com",
            pass = "123456",
            confirm = "999999"
        )

        advanceUntilIdle()

        val ui = vm.ui.value
        assertFalse(ui.success)
        assertNotNull(ui.error)
    }
}
