package com.example.myapplication7.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication7.ui.common.StyledButton
import com.example.myapplication7.ui.common.StyledTextField
import com.example.myapplication7.ui.viewmodel.AuthUiState
import com.example.myapplication7.ui.components.ThemeToggleButton

@Composable
fun LoginScreen(
    navController: NavController,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        ThemeToggleButton(
            darkTheme = darkTheme,
            onToggle = onToggleTheme,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = 1000))
            ) {
                Text("Welcome back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = 1000, delayMillis = 200))
            ) {
                Text("Log in to continue your study journey.", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 400))
            ) {
                StyledTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError ?: ""
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 600))
            ) {
                StyledTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError ?: "",
                    isPasswordField = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 800))
            ) {
                TextButton(
                    onClick = { navController.navigate("forgot_password") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password?")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 1000))
            ) {
                StyledButton(
                    text = "Log In",
                    onClick = onLoginClick,
                    isLoading = uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 1200))
            ) {
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Don't have an account? Sign Up")
                }
            }
        }
    }
}