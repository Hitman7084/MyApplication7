package com.example.myapplication7.ui.screens.bot

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.myapplication7.ui.components.SidebarScaffold

@Composable
fun BotScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    SidebarScaffold(
        navController = navController,
        title = "Bot",
        onLogout = onLogout,
        darkTheme = darkTheme,
        onToggleTheme = onToggleTheme
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(text = "Bot Screen")
        }
    }
}
