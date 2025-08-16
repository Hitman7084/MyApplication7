package com.example.myapplication7.net

import com.example.myapplication7.BuildConfig

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


data class NetMsg(val role: String, val content: String)
data class ChatReq(val messages: List<NetMsg>)
data class ChatRes(val text: String)

interface ChatApi {
  @POST("chat")
  suspend fun chat(@Body body: ChatReq): ChatRes
}

object Api {
  private val client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()
  private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  val chat: ChatApi = Retrofit.Builder()
    .baseUrl(BuildConfig.CHAT_BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))

    .build()
    .create(ChatApi::class.java)
}
