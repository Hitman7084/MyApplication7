package com.example.myapplication7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
            StudyFocusTheme {
                // Main App Navigation
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    // Get an instance of the ViewModel, scoped to this NavHost
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        // Authentication Flow Route
        composable(Screen.Auth.route) {
            // A separate NavHost for auth screens
            AuthNavHost(navController = rememberNavController(), onLoginSuccess = {
                // On success, navigate to profile and clear the auth back stack
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            })
        }
        // Profile Screen Route
        composable(Screen.Profile.route) {
            // Pass the logout logic to the HomeScreen
            HomeScreen(navController = navController, onLogout = {
                // 1. Call the logout function on the ViewModel to reset state
                authViewModel.logout()
                // 2. Navigate back to the login screen
                navController.navigate(Screen.Auth.route) {
                    // Clear the back stack so the user can't press "back" to get into the app
                    popUpTo(Screen.Profile.route) { inclusive = true }
                }
            })
        }
        // Calendar Screen Route
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        // Bot Screen Route
        composable(Screen.Bot.route) {
            BotScreen()
        }
    }
}