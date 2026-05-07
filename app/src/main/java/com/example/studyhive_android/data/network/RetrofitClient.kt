package com.example.studyhive_android.data.network

import android.content.Context
import com.example.studyhive_android.BuildConfig
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit singleton.
 *
 * Mirrors the web front-end's apiClient.ts:
 *
 *   apiClient.interceptors.request.use(async (config) => {
 *       const { data: { session } } = await supabase.auth.getSession();
 *       if (session?.access_token) {
 *           config.headers.Authorization = `Bearer ${session.access_token}`;
 *       }
 *       return config;
 *   });
 *
 * The [authInterceptor] does exactly the same thing — it fetches the current
 * Supabase session's access token and attaches it as a Bearer header on every
 * request to the Spring Boot API.
 */
object RetrofitClient {

    // Resolved in init() from BuildConfig.API_BASE_URL
    private var baseUrl: String = "https://api.studyhive.app/"

    /**
     * Call once from [StudyHiveApp.onCreate] with the application context.
     * The context is only needed if you want to persist tokens via DataStore
     * (currently the Supabase SDK manages its own session storage).
     */
    fun init(context: Context) {
        // No-op for now; hook is here so we can add DataStore later if needed.
        baseUrl = BuildConfig.API_BASE_URL.ifBlank { "https://api.studyhive.app/" }
    }

    // ── Auth interceptor ─────────────────────────────────────────────────
    // Fetches the live Supabase JWT on every request (same as web interceptor)
    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking { SupabaseClient.getAccessToken() }
        val request = chain.request().newBuilder()
            .apply {
                if (!token.isNullOrBlank()) {
                    header("Authorization", "Bearer $token")
                }
            }
            .header("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    // Only log in debug builds
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)          // auth first
            .addInterceptor(loggingInterceptor)       // then log
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ── Typed API services ───────────────────────────────────────────────
    val authApi:    AuthApi    by lazy { retrofit.create(AuthApi::class.java) }
    val groupApi:   GroupApi   by lazy { retrofit.create(GroupApi::class.java) }
    val sessionApi: SessionApi by lazy { retrofit.create(SessionApi::class.java) }
    val courseApi:  CourseApi  by lazy { retrofit.create(CourseApi::class.java) }
    val userApi:    UserApi    by lazy { retrofit.create(UserApi::class.java) }
}