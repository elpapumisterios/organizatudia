package com.example.organizatudia.tasks

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.organizatudia.features.tasks.data.local.AppDatabase
import com.example.organizatudia.features.tasks.data.repository.RoomTaskRepository
import com.example.organizatudia.features.tasks.domain.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


//“Verifica que una tarea se guarda y se recupera correctamente desde la base de datos.”
class TaskRoomIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RoomTaskRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomTaskRepository(db.taskDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertTask_savesInDatabase() = runBlocking {
        val task = Task(
            title = "Test",
            description = "Descripción",
            date = "2026-01-08",
            time = "10:00",
            category = "General",
            email = "test@test.com",
            priority = "Media",
            isCompleted = false,
            isArchived = false
        )

        repository.insertTask(task)

        val tasks = repository.getTasks().first()
        assertEquals(1, tasks.size)
    }
}
