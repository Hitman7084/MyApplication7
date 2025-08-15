package com.example.myapplication7.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication7.ui.common.StyledButton
import com.example.myapplication7.ui.common.StyledTextField
import com.example.myapplication7.ui.viewmodel.AuthUiState
import com.example.myapplication7.ui.components.ThemeToggleButton
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onSendLinkClick: () -> Unit,
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = 1000))
            ) {
                Text("Reset Password", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = 1000, delayMillis = 200))
            ) {
                Text(
                    "Enter your email and we'll send you a link to reset your password.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(visible = uiState.passwordResetLinkSent) {
                Text(
                    "✅ A password reset link has been sent to your email.",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

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

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 600))
            ) {
                StyledButton(
                    text = "Send Reset Link",
                    onClick = onSendLinkClick,
                    isLoading = uiState.isLoading,
                    enabled = !uiState.passwordResetLinkSent
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}