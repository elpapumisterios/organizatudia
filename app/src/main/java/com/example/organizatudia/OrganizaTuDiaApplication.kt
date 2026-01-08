package com.example.organizatudia

import android.app.Application
import com.example.organizatudia.data.repository.TaskRepositoryProvider
import com.example.organizatudia.framework.di.AppContainer

class OrganizaTuDiaApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        // ✅ Tu DI principal
        container = AppContainer(applicationContext)

        // ✅ ESTO evita el crash de Logros/Perfil
        TaskRepositoryProvider.init(applicationContext)
    }
}
