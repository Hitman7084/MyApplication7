package com.example.myapplication7.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository responsible for handling authentication via Supabase.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val client: SupabaseClient
) {

    /**
     * Log in an existing user using email and password.
     */
    suspend fun login(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Create a new user account using email and password.
     */
    suspend fun signup(email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Send a password reset link to the provided email address.
     */
    suspend fun sendPasswordReset(email: String) {
        client.auth.resetPasswordForEmail(email)
    }

    /**
     * Sign out the currently logged in user.
     */
    suspend fun logout() {
        client.auth.signOut()
    }
}

