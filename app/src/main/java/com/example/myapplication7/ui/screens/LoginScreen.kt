package com.example.myapplication7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication7.ui.common.StyledButton
import com.example.myapplication7.ui.common.StyledTextField
import com.example.myapplication7.ui.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    navController: NavController,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Log in to continue your study journey.", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(48.dp))

        StyledTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError ?: ""
        )

        Spacer(modifier = Modifier.height(16.dp))

        StyledTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError ?: "",
            isPasswordField = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate("forgot_password") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Log In",
            onClick = onLoginClick,
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign Up")
        }
    }
}