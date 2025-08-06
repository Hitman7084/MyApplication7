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

    NavHost(navController = navController, startDestination = "auth_flow") {
        // Authentication Flow Route
        composable("auth_flow") {
            // A separate NavHost for auth screens
            AuthNavHost(navController = rememberNavController(), onLoginSuccess = {
                // On success, navigate to home and clear the auth back stack
                navController.navigate("home") {
                    popUpTo("auth_flow") { inclusive = true }
                }
            })
        }
        // Home Screen Route
        composable("home") {
            // Pass the logout logic to the HomeScreen
            HomeScreen(onLogout = {
                // 1. Call the logout function on the ViewModel to reset state
                authViewModel.logout()
                // 2. Navigate back to the login screen
                navController.navigate("auth_flow") {
                    // Clear the back stack so the user can't press "back" to get into the app
                    popUpTo("home") { inclusive = true }
                }
            })
        }
    }
}