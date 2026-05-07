package com.example.studyhive_android.data.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient

/**
 * Singleton Supabase client — Android equivalent of:
 *
 *   // studyhive-web/src/lib/supabase.ts
 *   export const supabase = createClient(supabaseUrl, supabaseAnonKey)
 *
 * Initialise once from [StudyHiveApplication.onCreate] by calling [SupabaseClient.init].
 */
object SupabaseClient {

    private var _client: SupabaseClient? = null

    val client: SupabaseClient
        get() = requireNotNull(_client) {
            "SupabaseClient not initialised. Call SupabaseClient.init() in Application.onCreate()."
        }

    /** The [Auth] plugin — used for sign-in, sign-out, session retrieval. */
    val auth get() = client.auth

    /**
     * @param supabaseUrl     From BuildConfig.SUPABASE_URL  (e.g. "https://xyz.supabase.co")
     * @param supabaseAnonKey From BuildConfig.SUPABASE_ANON_KEY
     */
    fun init(supabaseUrl: String, supabaseAnonKey: String) {
        if (_client != null) return          // already initialised

        // FIX: Supabase Kotlin SDK v3 renamed the parameter from
        //      supabaseAnonKey → supabaseKey.
        _client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseAnonKey
        ) {
            install(Auth) {
                // Deep-link scheme registered in AndroidManifest.xml
                // Must match the Redirect URL configured in Supabase Dashboard →
                // Authentication → URL Configuration → Redirect URLs
                scheme = "studyhive"
                host   = "login-callback"
                defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
            }
        }
    }

    /** Returns the current JWT access token, or null if not signed in. */
    suspend fun getAccessToken(): String? =
        runCatching { auth.currentSessionOrNull()?.accessToken }.getOrNull()
}