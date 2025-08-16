package com.example.myapplication7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.myapplication7.navigation.AuthNavHost
import com.example.myapplication7.navigation.Screen
import com.example.myapplication7.ui.chat.ChatScreen
import com.example.myapplication7.ui.chat.ConversationListScreen
import com.example.myapplication7.ui.chat.ChatViewModel
import com.example.myapplication7.ui.chat.ConversationListViewModel
import com.example.myapplication7.ui.screens.calendar.CalendarScreen
import com.example.myapplication7.ui.screens.home.HomeScreen
import com.example.myapplication7.di.ChatObjects
import com.example.myapplication7.ui.theme.StudyFocusTheme
import com.example.myapplication7.ui.viewmodel.AuthViewModel
import com.example.myapplication7.App
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
        composable(Screen.ChatList.route) {
            val context = LocalContext.current
            val app = context.applicationContext as App
            val vm: ConversationListViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ConversationListViewModel(ChatObjects.repo(app)) as T
            })
            ConversationListScreen(vm) { c ->
                navController.navigate(Screen.Chat.createRoute(c.id))
            }
        }
        composable(
            Screen.Chat.route,
            arguments = listOf(navArgument("cid") { type = NavType.StringType })
        ) { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid")!!
            val context = LocalContext.current
            val app = context.applicationContext as App
            val vm: ChatViewModel = viewModel(key = "chat-$cid", factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ChatViewModel(ChatObjects.repo(app), cid) as T
            })
            ChatScreen(vm) { navController.popBackStack() }
        }
    }
}