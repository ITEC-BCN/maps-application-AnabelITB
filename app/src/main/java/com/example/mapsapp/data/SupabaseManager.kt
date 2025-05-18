package com.example.mapsapp.data

import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.AuthState
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

class SupabaseManager {
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
    val client = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Postgrest)
        install(Storage)
        install(Auth) {
            autoLoadFromStorage = true
        }
    }

    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    fun retrieveCurrentSession(): UserSession? {
        val session = client.auth.currentSessionOrNull()
        return session
    }

    fun refreshSession(): AuthState {
        try {
            client.auth.currentSessionOrNull()
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }
}
