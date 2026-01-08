package com.example.organizatudia.features.tasks.presentation.createtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizatudia.framework.di.AppContainer
//cómo se construye” el //Ver modelo con sus dependencias.

//“La Screen solo dibuja y manda eventos. El ViewModel maneja estado y lógica.
// La Factory inyecta dependencias desde el contenedor para crear el ViewModel
// correctamente.”
class CreateTaskViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            return CreateTaskViewModel(
                createTaskUseCase = container.createTaskUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
