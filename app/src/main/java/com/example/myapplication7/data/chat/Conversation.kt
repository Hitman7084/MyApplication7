package com.example.myapplication7.data.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Conversation(
  @PrimaryKey val id: String = UUID.randomUUID().toString(),
  val title: String = "New Chat",
  val createdAt: Long = System.currentTimeMillis()
)
