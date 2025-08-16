package com.example.myapplication7.data.chat

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Conversation::class, MessageEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
  abstract fun chatDao(): ChatDao
}
