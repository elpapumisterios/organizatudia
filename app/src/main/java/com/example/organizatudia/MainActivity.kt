package com.example.organizatudia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.organizatudia.presentation.home.BottomNavigationBar
import com.example.organizatudia.presentation.navigation.AppNavHost
import com.example.organizatudia.presentation.navigation.Screen
import com.example.organizatudia.ui.theme.OrganizaTuDiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrganizaTuDiaTheme {
                OrganizaTuDiaApp()
            }
        }
    }
}

@Composable
fun OrganizaTuDiaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Rutas donde se muestra la barra inferior
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
