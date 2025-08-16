package com.example.myapplication7.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(vm: ChatViewModel, onBack: () -> Unit) {
  val ui by vm.ui.collectAsState()
  LaunchedEffect(Unit) { vm.load() }
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Chat") },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } },
        actions = { TextButton(onClick = vm::clearHistory) { Text("Clear") } }
      )
    }
  ) { pad ->
    Column(Modifier.padding(pad).fillMaxSize()) {
      LazyColumn(Modifier.weight(1f).padding(12.dp)) {
        items(ui.messages, key = { it.id }) { m ->
          val isUser = m.role == "user"
          Row(Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
            Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) {
              Text(m.text, Modifier.padding(12.dp))
            }
          }
          Spacer(Modifier.height(6.dp))
        }
      }
      ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp)) }
      Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(value = ui.input, onValueChange = vm::onInputChange, modifier = Modifier.weight(1f), placeholder = { Text("Type your message…") }, enabled = !ui.sending)
        Spacer(Modifier.width(8.dp))
        Button(onClick = vm::send, enabled = !ui.sending) { Text(if (ui.sending) "…" else "Send") }
      }
    }
  }
}
