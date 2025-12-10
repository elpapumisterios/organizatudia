package com.example.organizatudia.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.organizatudia.presentation.achievements.AchievementsScreen
import com.example.organizatudia.presentation.archived.ArchivedTasksScreen
import com.example.organizatudia.presentation.createtask.CreateTaskScreen
import com.example.organizatudia.presentation.home.HomeScreen
import com.example.organizatudia.presentation.auth.LoginScreen
import com.example.organizatudia.presentation.profile.ProfileScreen
import com.example.organizatudia.presentation.auth.RegisterScreen
import com.example.organizatudia.presentation.settings.SettingsScreen
import com.example.organizatudia.presentation.welcome.WelcomeScreen

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
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onGoToRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
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
                onTaskSaved = { navController.popBackStack() }
            )
        }
    }
}
