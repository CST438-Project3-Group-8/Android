package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.*

import retrofit2.Response
import retrofit2.http.*

// ── SessionApi.kt ─────────────────────────────────────────────────────────────
interface SessionApi {
    @GET("api/sessions/{id}")
    suspend fun getSessionById(@Path("id") id: Int): Response<SessionDto>

    @GET("api/sessions/group/{groupId}")
    suspend fun getSessionsByGroup(@Path("groupId") groupId: Int): Response<List<SessionDto>>

    @POST("api/sessions")
    suspend fun createSession(@Body request: CreateSessionRequest): Response<SessionDto>

    @PUT("api/sessions/{id}")
    suspend fun updateSession(
        @Path("id") id: Int,
        @Body request: CreateSessionRequest
    ): Response<SessionDto>

    @DELETE("api/sessions/{id}")
    suspend fun deleteSession(@Path("id") id: Int): Response<Unit>
}

