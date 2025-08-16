package com.example.myapplication7.data.chat

import androidx.room.*

@Dao
interface ChatDao {
  @Insert fun insertConversation(c: Conversation)
  @Query("SELECT * FROM Conversation ORDER BY createdAt DESC")
  suspend fun listConversations(): List<Conversation>
  @Delete fun deleteConversation(c: Conversation)

  @Insert fun insertMessage(m: MessageEntity)
  @Query("SELECT * FROM MessageEntity WHERE conversationId = :cid ORDER BY createdAt ASC")
  suspend fun messagesFor(cid: String): List<MessageEntity>
  @Query("DELETE FROM MessageEntity WHERE conversationId = :cid")
  suspend fun clearMessages(cid: String)
}
