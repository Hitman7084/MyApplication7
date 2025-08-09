package com.example.myapplication7.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth_flow")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}
