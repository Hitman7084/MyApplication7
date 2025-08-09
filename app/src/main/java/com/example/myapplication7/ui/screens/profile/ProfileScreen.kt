package com.example.myapplication7.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication7.ui.common.ProfilePictureSelector
import com.example.myapplication7.ui.viewmodel.ProfileViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val boards = listOf("CBSE", "ICSE", "State")
    val streams = listOf("Science", "Commerce", "Arts")
    val exams = listOf("JEE", "NEET", "Boards")
    val classes = listOf("10", "11", "12")

    var boardExpanded by remember { mutableStateOf(false) }
    var streamExpanded by remember { mutableStateOf(false) }
    var examExpanded by remember { mutableStateOf(false) }
    var classExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfilePictureSelector(
            options = listOf(Icons.Default.Person, Icons.Default.Star, Icons.Default.Face),
            selectedOption = state.selectedPicture,
            onOptionSelected = viewModel::onProfilePicSelected
        )

        ExposedDropdownMenuBox(expanded = boardExpanded, onExpandedChange = { boardExpanded = !boardExpanded }) {
            OutlinedTextField(
                value = state.board,
                onValueChange = {},
                readOnly = true,
                label = { Text("Board") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = boardExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = boardExpanded, onDismissRequest = { boardExpanded = false }) {
                boards.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        viewModel.onBoardChange(it)
                        boardExpanded = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = streamExpanded, onExpandedChange = { streamExpanded = !streamExpanded }) {
            OutlinedTextField(
                value = state.stream,
                onValueChange = {},
                readOnly = true,
                label = { Text("Stream") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = streamExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = streamExpanded, onDismissRequest = { streamExpanded = false }) {
                streams.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        viewModel.onStreamChange(it)
                        streamExpanded = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = examExpanded, onExpandedChange = { examExpanded = !examExpanded }) {
            OutlinedTextField(
                value = state.exam,
                onValueChange = {},
                readOnly = true,
                label = { Text("Exam") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = examExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = examExpanded, onDismissRequest = { examExpanded = false }) {
                exams.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        viewModel.onExamChange(it)
                        examExpanded = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = classExpanded, onExpandedChange = { classExpanded = !classExpanded }) {
            OutlinedTextField(
                value = state.className,
                onValueChange = {},
                readOnly = true,
                label = { Text("Class") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = classExpanded, onDismissRequest = { classExpanded = false }) {
                classes.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        viewModel.onClassChange(it)
                        classExpanded = false
                    })
                }
            }
        }

        DatePickerField(label = "Start Date", date = state.startDate, onDateSelected = viewModel::onStartDateChange)
        DatePickerField(label = "End Date", date = state.endDate, onDateSelected = viewModel::onEndDateChange)

        Button(
            onClick = viewModel::saveProfile,
            enabled = state.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(label: String, date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.toEpochDay() * 24L * 60L * 60L * 1000L)

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateSelected(selected)
                    }
                    showDialog = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = date.toString(),
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().clickable { showDialog = true }
    )
}
