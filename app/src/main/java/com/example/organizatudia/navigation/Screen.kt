package com.example.organizatudia.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Achievements : Screen("achievements")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    object ArchivedTasks : Screen("archived_tasks")
    object CreateTask : Screen("create_task")
}