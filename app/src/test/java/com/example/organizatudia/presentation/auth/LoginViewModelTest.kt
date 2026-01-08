package com.example.organizatudia.presentation.auth

import com.example.organizatudia.MainDispatcherRule
import com.example.organizatudia.fakes.FakeAuthRepository
import com.example.organizatudia.features.auth.data.AuthRepositoryProvider
import com.example.organizatudia.features.auth.presentation.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

//Login con credenciales válidas
//
//Login con campos vacíos
//
//Manejo de errores de autenticación

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `si email o pass estan vacios muestra error`() = runTest {
        val fakeRepo = FakeAuthRepository()
        AuthRepositoryProvider.setForTests(fakeRepo)


        val vm = LoginViewModel(fakeRepo)

        vm.login(email = "", pass = "")
        advanceUntilIdle()

        val ui = vm.ui.value
        assertFalse(ui.success)
        assertNotNull(ui.error)
    }
}
