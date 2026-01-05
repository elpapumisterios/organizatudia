package com.example.organizatudia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.domain.model.Task
import com.example.organizatudia.domain.usecase.GetTaskByIdUseCase
import com.example.organizatudia.domain.usecase.GetTasksUseCase
import com.example.organizatudia.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class TaskUiModel(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val category: String,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false
)

data class HomeUiState(
    val tasks: List<TaskUiModel> = emptyList(),        // Vigentes (no vencidas)
    val overdueTasks: List<TaskUiModel> = emptyList(), // Vencidas y no completadas
    val progress: Float = 0f,                          // 0..1
    val progressText: String = "0/100%",
    val level: String = "Empezando",
    val doneToday: Int = 0,
    val totalToday: Int = 0
)

class HomeViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Formatos soportados para comparar fechas
    // OJO: la timeZone puede cambiar (emulador/PC). Por eso la re-seteamos en parseDate().
    private val dateFormats: List<SimpleDateFormat> = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()),
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    ).onEach {
        it.isLenient = false
        it.timeZone = TimeZone.getDefault()
    }

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            getTasksUseCase().collect { tasks ->
                val now = Date()

                // 1) Solo NO archivadas
                val activeTasks = tasks.filter { !it.isArchived }

                // 2) Vencidas (no completadas) y vigentes
                val overdue = activeTasks
                    .filter { !it.isCompleted && isOverdue(it, now) }
                    .sortedBy { dueDateTimeOrNull(it)?.time ?: Long.MAX_VALUE }

                val overdueIds = overdue.map { it.id }.toHashSet()

                val current = activeTasks
                    .filter { it.id !in overdueIds }
                    .sortedBy { dueDateTimeOrNull(it)?.time ?: Long.MAX_VALUE }

                // 3) Progreso SOLO con tareas de HOY (no archivadas)
                val todayTasks = activeTasks.filter { isForToday(it.date, now) }
                val progressInfo = calculateProgress(todayTasks)

                // 4) Auto-archivar vencidas después de 24h (y NO completadas)
                autoArchiveExpired(overdue, now)

                _uiState.update { currentState ->
                    currentState.copy(
                        tasks = current.map { it.toUiModel() },
                        overdueTasks = overdue.map { it.toUiModel() },
                        progress = progressInfo.progress,
                        progressText = progressInfo.progressText,
                        level = progressInfo.level,
                        doneToday = progressInfo.doneToday,
                        totalToday = progressInfo.totalToday
                    )
                }
            }
        }
    }

    fun onTaskCheckedChanged(taskId: String, isChecked: Boolean) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId) ?: return@launch
            updateTaskUseCase(task.copy(isCompleted = isChecked))
        }
    }

    /**
     * Archivar solo si está completada
     */
    fun onArchiveTask(taskId: String) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId) ?: return@launch
            if (!task.isCompleted) return@launch
            updateTaskUseCase(task.copy(isArchived = true))
        }
    }

    private data class ProgressInfo(
        val progress: Float,
        val progressText: String,
        val level: String,
        val doneToday: Int,
        val totalToday: Int
    )

    private fun calculateProgress(todayTasks: List<Task>): ProgressInfo {
        val total = todayTasks.size
        if (total == 0) {
            return ProgressInfo(
                progress = 0f,
                progressText = "0/100%",
                level = "Empezando",
                doneToday = 0,
                totalToday = 0
            )
        }

        val done = todayTasks.count { it.isCompleted }
        val p = (done.toFloat() / total.toFloat()).coerceIn(0f, 1f)
        val percent = (p * 100).toInt()

        val level = when {
            p > 0.66f -> "Extraordinario"
            p > 0.33f -> "Brillante"
            else -> "Empezando"
        }

        return ProgressInfo(
            progress = p,
            progressText = "$percent/100%",
            level = level,
            doneToday = done,
            totalToday = total
        )
    }

    private fun Task.toUiModel(): TaskUiModel =
        TaskUiModel(
            id = id,
            title = title,
            date = date,
            time = time,
            category = category,
            isCompleted = isCompleted,
            isArchived = isArchived
        )

    private fun isForToday(raw: String, now: Date): Boolean {
        val trimmed = raw.trim()
        if (trimmed.isBlank()) return true
        val parsed = parseDate(trimmed) ?: return false
        return sameDay(parsed, now)
    }

    private fun parseDate(raw: String): Date? {
        val tz = TimeZone.getDefault()
        for (fmt in dateFormats) {
            try {
                fmt.timeZone = tz
                return fmt.parse(raw)
            } catch (_: Exception) {}
        }
        return null
    }

    private fun sameDay(d1: Date, d2: Date): Boolean {
        val tz = TimeZone.getDefault()
        val c1 = Calendar.getInstance(tz).apply { time = d1 }
        val c2 = Calendar.getInstance(tz).apply { time = d2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * Define vencimiento usando:
     * - date + (hora FIN si existe en "HH:mm - HH:mm")
     * - si no hay hora => 23:59
     */
    private fun dueDateTimeOrNull(task: Task): Date? {
        val dateOnly = parseDate(task.date.trim()) ?: return null

        // 👈 USAMOS HORA DE INICIO, NO DE FIN
        val start = extractStartTime(task.time)

        val tz = TimeZone.getDefault()
        val cal = Calendar.getInstance(tz).apply {
            time = dateOnly
            set(Calendar.HOUR_OF_DAY, start.first)
            set(Calendar.MINUTE, start.second)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return cal.time
    }

    private fun extractStartTime(rawTime: String): Pair<Int, Int> {
        val t = rawTime.trim()
        if (t.isBlank() || t.equals("Todo el día", true)) return 23 to 59

        val timeRegex = Regex("""\b([01]?\d|2[0-3]):[0-5]\d\b""")
        val matches = timeRegex.findAll(t).map { it.value }.toList()
        val first = matches.firstOrNull() ?: return 23 to 59

        return parseHHmm(first) ?: (23 to 59)
    }

    private fun isOverdue(task: Task, now: Date): Boolean {
        val due = dueDateTimeOrNull(task) ?: return false

        // ⏱ margen de gracia: 1 minuto
        val graceMs = 60_000L

        android.util.Log.d(
            "OVERDUE_CHECK",
            "now=$now due=$due time=${task.time}"
        )

        return now.time > (due.time + graceMs)
    }



    /**
     * Si pasaron 24h desde el vencimiento y sigue NO completada => archivar automático.
     */
    private suspend fun autoArchiveExpired(overdueTasks: List<Task>, now: Date) {
        overdueTasks.forEach { t ->
            val due = dueDateTimeOrNull(t) ?: return@forEach
            val diffMs = now.time - due.time
            val hours = diffMs / (1000L * 60L * 60L)
            if (hours >= 24 && !t.isArchived && !t.isCompleted) {
                updateTaskUseCase(t.copy(isArchived = true))
            }
        }
    }

    private fun extractEndTime(rawTime: String): Pair<Int, Int> {
        val t = rawTime.trim()
        if (t.isBlank() || t.equals("Todo el día", true)) return 23 to 59

        // 1) Intentar "HH:mm" (soporta cosas como "11:00–11:30", "11:00 - 11:30", "11:00 a. m.")
        val timeRegex = Regex("""([01]?\d|2[0-3]):([0-5]\d)""")
        val matches = timeRegex.findAll(t).toList()
        if (matches.isNotEmpty()) {
            val last = matches.last()
            val h = last.groupValues[1].toInt()
            val m = last.groupValues[2].toInt()
            return h to m
        }

        // 2) Si viene solo la hora "11" o "8" (sin minutos), tomar esa hora y minuto 0
        val hourOnlyRegex = Regex("""\b([01]?\d|2[0-3])\b""")
        val hourMatch = hourOnlyRegex.find(t)
        if (hourMatch != null) {
            val h = hourMatch.groupValues[1].toInt()
            return h to 0
        }

        // 3) Si no se puede parsear, asumir fin de día (nunca la marca vencida por error)
        return 23 to 59
    }


    private fun parseHHmm(s: String): Pair<Int, Int>? {
        val parts = s.trim().split(":")
        if (parts.size != 2) return null
        val h = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        if (h !in 0..23 || m !in 0..59) return null
        return h to m
    }
}
