package com.example.myapplication7.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val selectedPicture: ImageVector = Icons.Default.Person,
    val isLoading: Boolean = false,
    val passwordResetLinkSent: Boolean = false,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onProfilePicSelected(picture: ImageVector) {
        _uiState.update { it.copy(selectedPicture = picture) }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            // ... (login logic)
            _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
        }
    }

    fun onSignupClicked() {
        viewModelScope.launch {
            // ... (signup logic)
            _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
        }
    }

    fun onSendResetLink() {
        viewModelScope.launch {
            // ... (reset link logic)
            _uiState.update { it.copy(isLoading = false, passwordResetLinkSent = true) }
        }
    }
    fun logout() {
        // Reset the entire state to its initial default values
        _uiState.value = AuthUiState()
    }
}