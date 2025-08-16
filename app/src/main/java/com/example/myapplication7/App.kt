package com.example.myapplication7

import android.app.Application
import androidx.room.Room
import com.example.myapplication7.data.chat.AppDb
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    lateinit var db: AppDb

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDb::class.java, "chat.db").build()
    }
}