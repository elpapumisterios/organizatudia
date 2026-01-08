package com.example.organizatudia.auth

import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.example.organizatudia.core.ui.theme.OrganizaTuDiaTheme
import com.example.organizatudia.features.auth.presentation.login.LoginScreen
import com.example.organizatudia.framework.di.AppContainer
import com.example.organizatudia.framework.di.LocalAppContainer
import org.junit.Rule
import org.junit.Test
//// Verifica que la pantalla de Login se
// renderiza correctamente y muestra el botón de inicio de sesión.


//“Comprueba que la interfaz de login se muestra correctamente.”
class LoginScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginScreen_showsLoginButton() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val container = AppContainer(context)

        composeRule.setContent {
            CompositionLocalProvider(LocalAppContainer provides container) {
                OrganizaTuDiaTheme {
                    LoginScreen(
                        onLoginSuccess = {},
                        onGoRegister = {}
                    )
                }
            }
        }

        // En tu LoginScreen el botón tiene testTag("loginButton")
        composeRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }
}
