package com.example.organizatudia.framework.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer no fue provisto. Revisa MainActivity.")
}
