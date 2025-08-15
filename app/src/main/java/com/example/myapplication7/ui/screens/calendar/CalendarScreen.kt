package com.example.myapplication7.ui.screens.calendar

import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.myapplication7.ui.components.SidebarScaffold
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val tasks = remember { mutableStateMapOf<LocalDate, SnapshotStateList<String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var newTask by remember { mutableStateOf("") }

    SidebarScaffold(
        navController = navController,
        title = "Calendar",
        onLogout = onLogout,
        darkTheme = darkTheme,
        onToggleTheme = onToggleTheme
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AndroidView(
                factory = { ctx ->
                    CalendarView(ctx).apply {
                        date = selectedDate.value.toEpochDay() * 24 * 60 * 60 * 1000
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = selectedDate.value.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            val list = tasks.getOrPut(selectedDate.value) { mutableStateListOf() }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (list.isEmpty()) {
                        Text("No tasks yet", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        list.forEach { task ->
                            Text("• $task", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    if (list.size < 3) {
                        OutlinedButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Add Task")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    if (newTask.isNotBlank()) {
                        tasks.getOrPut(selectedDate.value) { mutableStateListOf() }.add(newTask.trim())
                    }
                    newTask = ""
                    showDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            title = { Text("New Task") },
            text = {
                TextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    placeholder = { Text("Enter task") }
                )
            }
        )
    }
}
