package com.example.myapplication7.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth_flow")
    data object Profile : Screen("profile")
    data object Calendar : Screen("calendar")
    data object ChatList : Screen("chat_list")
    data object Chat : Screen("chat/{cid}") {
        fun createRoute(cid: String) = "chat/$cid"
    }
}