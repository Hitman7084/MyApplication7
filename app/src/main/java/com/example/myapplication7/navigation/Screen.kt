package com.example.myapplication7.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth_flow")
    object Home : Screen("home")
    object Profile : Screen("profile")
}
