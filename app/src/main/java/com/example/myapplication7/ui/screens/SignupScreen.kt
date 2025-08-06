package com.example.myapplication7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication7.ui.common.ProfilePictureSelector
import com.example.myapplication7.ui.common.StyledButton
import com.example.myapplication7.ui.common.StyledTextField
import com.example.myapplication7.ui.viewmodel.AuthUiState

@Composable
fun SignupScreen(
    navController: NavController,
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPictureSelect: (ImageVector) -> Unit,
    onSignupClick: () -> Unit
) {
    val profilePictures = listOf(
        Icons.Default.Face,
        Icons.Default.Pets,
        Icons.Default.Spa,
        Icons.Default.SelfImprovement
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create your account", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        ProfilePictureSelector(
            options = profilePictures,
            selectedOption = uiState.selectedPicture,
            onOptionSelected = onPictureSelect
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = "Full Name",
            isError = uiState.nameError != null,
            errorMessage = uiState.nameError ?: ""
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Create Account",
            onClick = onSignupClick,
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Log In")
        }
    }
}