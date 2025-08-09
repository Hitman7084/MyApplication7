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
import com.example.myapplication7.ui.screens.home.HomeScreen // Corrected import path
import com.example.myapplication7.ui.screens.profile.ProfileScreen
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
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            AuthNavHost(navController = rememberNavController(), onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}