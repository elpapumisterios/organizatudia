package com.example.organizatudia.presentation.createtask

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.organizatudia.features.tasks.data.repository.TaskRepositoryProvider
import com.example.organizatudia.core.ui.theme.OrganizaTuDiaTheme
import com.example.organizatudia.features.tasks.presentation.createtask.CreateTaskScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateTaskScreenUiRealTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Inicializa el repo como lo hace MainActivity
        TaskRepositoryProvider.init(context)
    }

    @Test
    fun clickingSave_withEmptyTitle_showsValidationError() {
        composeRule.setContent {
            OrganizaTuDiaTheme {
                CreateTaskScreen(
                    onBackClick = {},
                    onTaskSaved = {}
                )
            }
        }

        composeRule.onNodeWithTag("saveTaskButton").performClick()
        composeRule.onNodeWithTag("createTaskErrorText").assertIsDisplayed()
    }

    @Test
    fun savingValidTask_callsOnTaskSaved() {
        var savedCalled = false

        composeRule.setContent {
            OrganizaTuDiaTheme {
                CreateTaskScreen(
                    onBackClick = {},
                    onTaskSaved = { savedCalled = true }
                )
            }
        }

        // escribir un título válido (lo único obligatorio)
        composeRule.onNodeWithTag("titleField")
            .performTextInput("Tarea de prueba")

        // guardar
        composeRule.onNodeWithTag("saveTaskButton")
            .performClick()

        // ✅ Espera REAL hasta que la app llame onTaskSaved()
        composeRule.waitUntil(timeoutMillis = 8_000) {
            savedCalled
        }
    }
}
