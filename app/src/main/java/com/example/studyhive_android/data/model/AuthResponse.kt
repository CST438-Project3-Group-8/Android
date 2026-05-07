package com.example.studyhive_android.data.model

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val major: String?,
    val bio: String?
)