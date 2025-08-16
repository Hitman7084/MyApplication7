package com.example.myapplication7.di

import com.example.myapplication7.App
import com.example.myapplication7.data.chat.ChatRepository

object ChatObjects {
  fun repo(app: App): ChatRepository = ChatRepository(app.db.chatDao())
}
