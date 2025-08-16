package com.example.myapplication7.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Date
import com.example.myapplication7.data.chat.Conversation

@Composable
fun ConversationListScreen(
  vm: ConversationListViewModel,
  onOpen: (Conversation) -> Unit
) {
  val ui by vm.ui.collectAsState()
  LaunchedEffect(Unit) { vm.load() }
  Scaffold(
    floatingActionButton = { FloatingActionButton(onClick = { vm.newChat(onOpen) }) { Text("+") } }
  ) { pad ->
    Column(Modifier.padding(pad)) {
      if (ui.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
      ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp)) }
      LazyColumn {
        items(ui.items, key = { it.id }) { c ->
          ListItem(
            headlineContent = { Text(c.title) },
            supportingContent = { Text(DateFormat.getDateTimeInstance().format(Date(c.createdAt))) },
            modifier = Modifier.fillMaxWidth().clickable { onOpen(c) }.padding(horizontal = 8.dp)
          )
          Divider()
        }
      }
    }
  }
}
