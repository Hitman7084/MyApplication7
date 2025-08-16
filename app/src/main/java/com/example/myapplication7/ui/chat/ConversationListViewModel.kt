package com.example.myapplication7.ui.chat

import com.example.myapplication7.data.chat.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConversationListUi(
  val items: List<Conversation> = emptyList(),
  val loading: Boolean = false,
  val error: String? = null
)

class ConversationListViewModel(private val repo: ChatRepository) : ViewModel() {
  val ui = MutableStateFlow(ConversationListUi(loading = true))

  fun load() = viewModelScope.launch {
    try { ui.update { it.copy(items = repo.listConversations(), loading = false) } }
    catch (e: Exception) { ui.update { it.copy(loading = false, error = e.message) } }
  }

  fun newChat(onCreated: (Conversation) -> Unit) = viewModelScope.launch {
    val c = repo.newConversation()
    load()
    onCreated(c)
  }

  fun delete(c: Conversation) = viewModelScope.launch { repo.deleteConversation(c); load() }
}
