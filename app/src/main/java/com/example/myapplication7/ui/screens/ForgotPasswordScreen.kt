package com.example.myapplication7.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication7.ui.common.StyledButton
import com.example.myapplication7.ui.common.StyledTextField
import com.example.myapplication7.ui.viewmodel.AuthUiState

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onSendLinkClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Reset Password", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(
            "Enter your email and we'll send you a link to reset your password.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        AnimatedVisibility(visible = uiState.passwordResetLinkSent) {
            Text(
                "✅ A password reset link has been sent to your email.",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        StyledTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError ?: ""
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Send Reset Link",
            onClick = onSendLinkClick,
            isLoading = uiState.isLoading,
            enabled = !uiState.passwordResetLinkSent
        )
    }
}