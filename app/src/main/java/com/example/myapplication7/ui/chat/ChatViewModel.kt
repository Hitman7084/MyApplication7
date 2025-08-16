package com.example.myapplication7.ui.chat

import com.example.myapplication7.data.chat.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUi(
  val conversationId: String,
  val messages: List<MessageEntity> = emptyList(),
  val input: String = "",
  val sending: Boolean = false,
  val error: String? = null
)

class ChatViewModel(
  private val repo: ChatRepository,
  private val conversationId: String
) : ViewModel() {
  val ui = MutableStateFlow(ChatUi(conversationId))

  fun load() = viewModelScope.launch {
    ui.update { it.copy(messages = repo.getMessages(conversationId)) }
  }

  fun onInputChange(v: String) { ui.update { it.copy(input = v) } }

  fun send() {
    val text = ui.value.input.trim()
    if (text.isEmpty() || ui.value.sending) return
    ui.update { it.copy(sending = true, input = "", error = null) }
    viewModelScope.launch {
      try { repo.sendMessage(conversationId, text); load() }
      catch (e: Exception) { ui.update { it.copy(error = e.message) } }
      finally { ui.update { it.copy(sending = false) } }
    }
  }

  fun clearHistory() = viewModelScope.launch { repo.clearConversation(conversationId); load() }
}
