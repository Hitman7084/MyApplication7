package com.example.myapplication7.net

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient

data class NetMsg(val role: String, val content: String)
data class ChatReq(val messages: List<NetMsg>)
data class ChatRes(val text: String)

interface ChatApi {
  @POST("chat")
  suspend fun chat(@Body body: ChatReq): ChatRes
}

object Api {
  private val client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()
  val chat: ChatApi = Retrofit.Builder()
    .baseUrl(BuildConfig.CHAT_BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(ChatApi::class.java)
}
