package com.example.myapplication7.data.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val conversationId: String,
  val role: String, // "user" | "assistant"
  val text: String,
  val createdAt: Long = System.currentTimeMillis()
)
