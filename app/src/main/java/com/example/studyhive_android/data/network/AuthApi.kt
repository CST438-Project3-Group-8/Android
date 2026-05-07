package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.LoginRequest
import com.example.studyhive_android.data.model.SignupRequest
import com.example.studyhive_android.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.*

// ── AuthApi.kt ───────────────────────────────────────────────────────────────
interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>
}