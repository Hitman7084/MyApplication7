package com.example.myapplication7.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ThemeToggleButton(
    darkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            imageVector = if (darkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            contentDescription = if (darkTheme) "Switch to light theme" else "Switch to dark theme"
        )
    }
}
