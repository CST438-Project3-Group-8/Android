package com.example.studyhive_android.data.repository

import com.example.studyhive_android.data.model.UserDto
import com.example.studyhive_android.data.network.RetrofitClient
import com.example.studyhive_android.data.network.SupabaseClient
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.Github
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Single source of truth for authentication.
 */
class AuthRepository {

    private val auth get() = SupabaseClient.auth
    private val userApi get() = RetrofitClient.userApi

    // ── Session state ────────────────────────────────────────────────────

    /**
     * Emits the current [UserSession] or null when signed out.
     *
     * FIX: Removed SessionStatus entirely — it moved packages in SDK 3.x and
     * the import resolves to nothing. Mapping any status-change event to
     * currentSessionOrNull() gives the same behaviour with no import needed.
     */
    val sessionFlow: Flow<UserSession?> = auth.sessionStatus.map {
        auth.currentSessionOrNull()
    }

    fun currentSession(): UserSession? = auth.currentSessionOrNull()

    // ── Sign-in methods ─────────────────────────────────────────────────

    suspend fun signInWithGoogle() { auth.signInWith(Google) }

    suspend fun signInWithGitHub() { auth.signInWith(Github) }

    suspend fun signInWithEmail(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * FIX: Use JsonPrimitive(fullName) explicitly.
     * SDK 3.x dropped the JsonObjectBuilder.put(String, String) overload so
     * passing a raw String causes "Argument type mismatch: actual kotlin.String
     * but kotlinx.serialization.json.JsonElement expected".
     */
    suspend fun signUpWithEmail(email: String, password: String, fullName: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("full_name", JsonPrimitive(fullName))
            }
        }
    }

    suspend fun signOut() { auth.signOut() }

    // ── Backend profile bootstrap ────────────────────────────────────────

    suspend fun bootstrapBackendProfile(): UserDto {
        val delays = listOf(400L, 900L, 1500L, 2200L)
        var lastError: Throwable = IllegalStateException("bootstrap never ran")
        for ((attempt, _) in (delays + listOf(0L)).withIndex()) {
            try {
                val response = userApi.createOrBootstrapProfile()
                if (response.isSuccessful) {
                    return response.body()
                        ?: throw IllegalStateException("Empty body from /api/user")
                }
                throw IllegalStateException("HTTP ${response.code()}: ${response.errorBody()?.string()}")
            } catch (e: Throwable) {
                lastError = e
                if (attempt < delays.size) delay(delays[attempt])
            }
        }
        throw lastError
    }

    // ── Helpers ─────────────────────────────────────────────────────────

    fun displayName(): String {
        val user = auth.currentUserOrNull() ?: return "User"
        return (user.userMetadata?.get("full_name") as? String)?.takeIf { it.isNotBlank() }
            ?: user.email?.substringBefore('@')
            ?: "User"
    }

    fun currentUserId(): String? = auth.currentUserOrNull()?.id
}