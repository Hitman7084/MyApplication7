package com.example.myapplication7.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication7.data.repository.Profile
import com.example.myapplication7.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

// Holds UI state for the profile screen
data class ProfileUiState(
    val selectedPicture: ImageVector = Icons.Default.Person,
    val board: String = "",
    val stream: String = "",
    val exam: String = "",
    val className: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now()
) {
    val isValid: Boolean
        get() = board.isNotBlank() && stream.isNotBlank() && exam.isNotBlank() &&
                className.isNotBlank() && !endDate.isBefore(startDate)
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onBoardChange(board: String) = _uiState.update { it.copy(board = board) }
    fun onStreamChange(stream: String) = _uiState.update { it.copy(stream = stream) }
    fun onExamChange(exam: String) = _uiState.update { it.copy(exam = exam) }
    fun onClassChange(className: String) = _uiState.update { it.copy(className = className) }
    fun onStartDateChange(date: LocalDate) = _uiState.update { it.copy(startDate = date) }
    fun onEndDateChange(date: LocalDate) = _uiState.update { it.copy(endDate = date) }
    fun onProfilePicSelected(picture: ImageVector) = _uiState.update { it.copy(selectedPicture = picture) }

    fun saveProfile() {
        val state = _uiState.value
        if (!state.isValid) return
        viewModelScope.launch {
            val profile = Profile(
                board = state.board,
                stream = state.stream,
                exam = state.exam,
                className = state.className,
                startDate = state.startDate.toString(),
                endDate = state.endDate.toString(),
                avatar = state.selectedPicture.name
            )
            repository.saveProfile(profile)
        }
    }
}
