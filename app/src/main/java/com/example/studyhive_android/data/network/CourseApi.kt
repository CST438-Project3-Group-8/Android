package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.*

import retrofit2.Response
import retrofit2.http.*

// ── CourseApi.kt ──────────────────────────────────────────────────────────────
interface CourseApi {
    @GET("api/courses")
    suspend fun getCourses(): Response<List<CourseDto>>
}
