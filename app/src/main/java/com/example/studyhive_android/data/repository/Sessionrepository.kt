package com.example.studyhive_android.data.repository

import com.example.studyhive_android.data.model.CreateSessionRequest
import com.example.studyhive_android.data.model.SessionDto
import com.example.studyhive_android.data.network.RetrofitClient

/**
 * Repository for study-session operations.
 *
 * Mirrors studyhive-web/src/api/sessionsApi.ts:
 *   getSessionsByGroup(), createSession(), updateSession(), deleteSession()
 */
class SessionRepository {

    private val api get() = RetrofitClient.sessionApi

    /** GET /api/sessions/group/{groupId} */
    suspend fun getSessionsByGroup(groupId: Int): List<SessionDto> {
        val response = api.getSessionsByGroup(groupId)
        return response.body() ?: emptyList()
    }

    /** GET /api/sessions/{id} */
    suspend fun getSessionById(id: Int): SessionDto? {
        val response = api.getSessionById(id)
        return if (response.isSuccessful) response.body() else null
    }

    /** POST /api/sessions */
    suspend fun createSession(request: CreateSessionRequest): SessionDto {
        val response = api.createSession(request)
        return response.body()
            ?: throw IllegalStateException("Failed to create session: HTTP ${response.code()}")
    }

    /** PUT /api/sessions/{id} */
    suspend fun updateSession(id: Int, request: CreateSessionRequest): SessionDto {
        val response = api.updateSession(id, request)
        return response.body()
            ?: throw IllegalStateException("Failed to update session: HTTP ${response.code()}")
    }

    /** DELETE /api/sessions/{id} */
    suspend fun deleteSession(id: Int) {
        api.deleteSession(id)
    }
}