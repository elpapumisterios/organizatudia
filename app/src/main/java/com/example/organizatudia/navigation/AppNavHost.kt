package com.example.organizatudia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.organizatudia.features.achievements.presentation.AchievementsScreen
import com.example.organizatudia.features.archived.presentation.ArchivedTasksScreen
import com.example.organizatudia.features.auth.presentation.login.LoginScreen
import com.example.organizatudia.features.auth.presentation.register.RegisterScreen
import com.example.organizatudia.features.tasks.presentation.createtask.CreateTaskScreen
import com.example.organizatudia.features.tasks.presentation.home.HomeScreen
import com.example.organizatudia.features.profile.presentation.ProfileScreen
import com.example.organizatudia.features.settings.presentation.SettingsScreen
import com.example.organizatudia.features.welcome.presentation.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onAddTaskClick = { navController.navigate(Screen.CreateTask.route) }
            )
        }

        composable(Screen.Achievements.route) {
            AchievementsScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onTasksClick = { navController.navigate(Screen.Home.route) },
                onTreeProgressClick = { navController.navigate(Screen.Home.route) }, // o futura pantalla de árbol
                onAchievementsClick = { navController.navigate(Screen.Achievements.route) },
                onNotificationsClick = { /* futura pantalla de notificaciones */ },
                onArchivedTasksClick = { navController.navigate(Screen.ArchivedTasks.route) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(Screen.ArchivedTasks.route) {
            ArchivedTasksScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateTask.route) {
            CreateTaskScreen(
                onBackClick = { navController.popBackStack() },
                onTaskSaved = { navController.popBackStack() }
            )
        }
    }
}
