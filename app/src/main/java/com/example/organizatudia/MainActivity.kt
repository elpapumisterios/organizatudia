package com.example.organizatudia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.organizatudia.framework.di.AppContainer
import com.example.organizatudia.framework.di.LocalAppContainer
import com.example.organizatudia.features.tasks.presentation.home.BottomNavigationBar
import com.example.organizatudia.navigation.AppNavHost
import com.example.organizatudia.navigation.Screen
import com.example.organizatudia.core.ui.theme.OrganizaTuDiaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // ✅ Un solo container para toda la app (DI manual, Clean Architecture friendly)
        val container = AppContainer(applicationContext)

        setContent {
            OrganizaTuDiaTheme {
                val container = AppContainer(applicationContext)
                CompositionLocalProvider(
                    LocalAppContainer provides container
                ) {
                    OrganizaTuDiaApp()
                }
            }
        }

    }
}

@Composable
fun OrganizaTuDiaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = setOf(
        Screen.Home.route,
        Screen.Achievements.route,
        Screen.Settings.route,
        Screen.Profile.route
    )
    val shouldShowBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
