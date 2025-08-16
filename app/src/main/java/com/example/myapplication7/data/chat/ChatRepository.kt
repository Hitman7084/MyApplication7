package com.example.myapplication7.data.chat

import com.example.myapplication7.net.Api
import com.example.myapplication7.net.ChatReq
import com.example.myapplication7.net.NetMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val dao: ChatDao) {

  suspend fun newConversation(): Conversation = withContext(Dispatchers.IO) {
    val c = Conversation()
    dao.insertConversation(c)
    c
  }

  suspend fun listConversations() = dao.listConversations()
  suspend fun getMessages(cid: String) = dao.messagesFor(cid)

  suspend fun sendMessage(cid: String, userText: String): MessageEntity = withContext(Dispatchers.IO) {
    dao.insertMessage(MessageEntity(conversationId = cid, role = "user", text = userText))
    val history = dao.messagesFor(cid).map { NetMsg(role = it.role, content = it.text) }
    val reply = Api.chat.chat(ChatReq(messages = history))
    val bot = MessageEntity(conversationId = cid, role = "assistant", text = reply.text)
    dao.insertMessage(bot)
    bot
  }

  suspend fun clearConversation(cid: String) { dao.clearMessages(cid) }
  suspend fun deleteConversation(c: Conversation) { dao.clearMessages(c.id); dao.deleteConversation(c) }
}
