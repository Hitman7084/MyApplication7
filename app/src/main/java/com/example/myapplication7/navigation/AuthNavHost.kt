package com.example.myapplication7.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication7.ui.screens.ForgotPasswordScreen
import com.example.myapplication7.ui.screens.LoginScreen
import com.example.myapplication7.ui.screens.SignupScreen
import com.example.myapplication7.ui.viewmodel.AuthViewModel

// Defines the routes for the authentication screens
sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login")
    data object Signup : AuthScreen("signup")
    data object ForgotPassword : AuthScreen("forgot_password")
}

// Manages navigation between the auth screens
@Composable
fun AuthNavHost(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    // The ViewModel is shared across all screens in this navigation graph
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Trigger navigation when login is successful
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(AuthScreen.Login.route) {
            LoginScreen(
                navController = navController,
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = viewModel::onLoginClicked
            )
        }
        composable(AuthScreen.Signup.route) {
            SignupScreen(
                navController = navController,
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onPictureSelect = viewModel::onProfilePicSelected,
                onSignupClick = viewModel::onSignupClicked
            )
        }
        composable(AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                navController = navController,
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onSendLinkClick = viewModel::onSendResetLink
            )
        }
    }
}