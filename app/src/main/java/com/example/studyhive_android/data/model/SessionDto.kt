package com.example.studyhive_android.data.model

data class SessionDto(
    val id: Int,
    val groupId: Int,
    val title: String,
    val topic: String?,
    val scheduledAt: String,
    val location: String?,
    val notes: String?,
    val durationMinutes: Int?
)

data class CreateSessionRequest(
    val groupId: Int,
    val title: String,
    val topic: String? = null,
    val scheduledAt: String,
    val location: String? = null,
    val notes: String? = null,
    val durationMinutes: Int? = null
)