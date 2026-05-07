package com.example.studyhive_android.data.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api.studyhive.app/" // replace with your actual base URL
    private const val PREFS_NAME = "studyhive_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"

    private var prefs: SharedPreferences? = null

    /** Call once from Application.onCreate() */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs?.edit()?.putString(KEY_ACCESS_TOKEN, token)?.apply()
    }

    fun clearToken() {
        prefs?.edit()?.remove(KEY_ACCESS_TOKEN)?.apply()
    }

    fun getToken(): String? = prefs?.getString(KEY_ACCESS_TOKEN, null)

    // ── OkHttp with auth interceptor ─────────────────────────────────────────

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = getToken()
        val request = if (token != null) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor) // remove in release builds
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // ── Gson ─────────────────────────────────────────────────────────────────

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // ── Retrofit singleton ───────────────────────────────────────────────────

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ── Typed API services ───────────────────────────────────────────────────

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val sessionApi: SessionApi by lazy { retrofit.create(SessionApi::class.java) }
    val groupApi: GroupApi by lazy { retrofit.create(GroupApi::class.java) }
    val courseApi: CourseApi by lazy { retrofit.create(CourseApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}