package com.example.myapplication7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel // Import hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication7.navigation.AuthNavHost
import com.example.myapplication7.navigation.Screen
import com.example.myapplication7.ui.screens.bot.BotScreen
import com.example.myapplication7.ui.screens.calendar.CalendarScreen
import com.example.myapplication7.ui.screens.home.HomeScreen // Corrected import path
import com.example.myapplication7.ui.theme.StudyFocusTheme
import com.example.myapplication7.ui.viewmodel.AuthViewModel // Import the ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemDarkTheme) }
            StudyFocusTheme(darkTheme = darkTheme) {
                // Main App Navigation
                AppNav(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

@Composable
fun AppNav(darkTheme: Boolean, onToggleTheme: () -> Unit) {
    val navController = rememberNavController()
    // Get an instance of the ViewModel, scoped to this NavHost
    val authViewModel: AuthViewModel = hiltViewModel()
    val logout: () -> Unit = {
        authViewModel.logout()
        navController.navigate(Screen.Auth.route) {
            popUpTo(Screen.Profile.route) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        // Authentication Flow Route
        composable(Screen.Auth.route) {
            // A separate NavHost for auth screens
            AuthNavHost(
                navController = rememberNavController(),
                onLoginSuccess = {
                    // On success, navigate to profile and clear the auth back stack
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        // Profile Screen Route
        composable(Screen.Profile.route) {
            HomeScreen(
                navController = navController,
                onLogout = logout,
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(Screen.Calendar.route) {
            CalendarScreen(
                navController = navController,
                onLogout = logout,
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(Screen.Bot.route) {
            BotScreen(
                navController = navController,
                onLogout = logout,
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    }
}