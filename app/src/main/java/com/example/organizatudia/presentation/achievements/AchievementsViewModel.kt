package com.example.organizatudia.presentation.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizatudia.data.repository.TaskRepositoryProvider
import com.example.organizatudia.domain.model.Achievement
import com.example.organizatudia.domain.usecase.GetAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AchievementsViewModel : ViewModel() {

    private val repository = TaskRepositoryProvider.taskRepository
    private val useCase = GetAchievementsUseCase()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            repository.getTasks().collect { tasks ->
                _achievements.value = useCase.calculateAchievements(tasks)
            }
        }
    }
}
