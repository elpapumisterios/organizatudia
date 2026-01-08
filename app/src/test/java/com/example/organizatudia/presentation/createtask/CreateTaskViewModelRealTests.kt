package com.example.organizatudia.presentation.createtask

import com.example.organizatudia.MainDispatcherRule
import com.example.organizatudia.features.tasks.data.repository.TaskRepositoryProvider
import com.example.organizatudia.features.tasks.domain.usecase.CreateTaskUseCase
import com.example.organizatudia.features.tasks.presentation.createtask.CreateTaskViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTaskViewModelRealTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        TaskRepositoryProvider.initInMemory()
    }

    private fun buildViewModel(): CreateTaskViewModel {
        val repo = TaskRepositoryProvider.taskRepository
        val createTaskUseCase = CreateTaskUseCase(repo)
        return CreateTaskViewModel(createTaskUseCase = createTaskUseCase)
    }

    @Test
    fun saveTask_blankTitle_setsError_andDoesNotInsert() = runTest {
        val vm = buildViewModel()

        vm.saveTask(
            title = "",
            email = "a@a.com",
            date = "2026-01-01",
            startTime = "10:00",
            endTime = "11:00",
            category = "Personal",
            priority = "Media",
            description = "desc"
        )

        advanceUntilIdle()

        assertEquals("El título es obligatorio.", vm.uiState.value.error)

        val tasks = TaskRepositoryProvider.taskRepository.getTasks().first()
        assertEquals(0, tasks.size)
    }

    @Test
    fun saveTask_endBeforeStart_setsError_andDoesNotInsert() = runTest {
        val vm = buildViewModel()

        vm.saveTask(
            title = "Tarea válida",
            email = "a@a.com",
            date = "2026-01-01",
            startTime = "12:00",
            endTime = "11:00",
            category = "Trabajo",
            priority = "Alta",
            description = "desc"
        )

        advanceUntilIdle()

        assertEquals(
            "La hora de fin no puede ser menor que la hora de inicio.",
            vm.uiState.value.error
        )

        val tasks = TaskRepositoryProvider.taskRepository.getTasks().first()
        assertEquals(0, tasks.size)
    }

    @Test
    fun saveTask_validData_insertsTask_andSetsSavedTrue() = runTest {
        val vm = buildViewModel()

        vm.saveTask(
            title = "Tarea real",
            email = "a@a.com",
            date = "2026-01-01",
            startTime = "10:00",
            endTime = "11:00",
            category = "Personal",
            priority = "Baja",
            description = "desc"
        )

        advanceUntilIdle()

        assertTrue(vm.uiState.value.saved)

        val tasks = TaskRepositoryProvider.taskRepository.getTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Tarea real", tasks.first().title)
        assertEquals("10:00 - 11:00", tasks.first().time)
    }
}
