package com.example.myapplication7.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication7.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

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
        val email = uiState.value.email.trim()
        val password = uiState.value.password.trim()
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be blank!!") }
            return
        }
        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password cannot be blank!!") }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.login(email, password)
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, passwordError = "Incorrect email or password") }
            }
        }
    }

    fun onSignupClicked() {
        val name = uiState.value.name.trim()
        val email = uiState.value.email.trim()
        val password = uiState.value.password.trim()
        var hasError = false
        if (name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name cannot be blank") }
            hasError = true
        }
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be blank") }
            hasError = true
        }
        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password cannot be blank") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signup(email, password)
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, emailError = e.message) }
            }
        }
    }

    fun onSendResetLink() {
        val email = uiState.value.email.trim()
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be blank") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.sendPasswordReset(email)
                _uiState.update { it.copy(isLoading = false, passwordResetLinkSent = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, emailError = e.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }
}