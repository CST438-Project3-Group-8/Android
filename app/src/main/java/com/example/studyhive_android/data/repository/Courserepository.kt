package com.example.studyhive_android.data.repository

import com.example.studyhive_android.data.model.CourseDto
import com.example.studyhive_android.data.network.RetrofitClient

/**
 * Repository for course listings.
 * Mirrors studyhive-web/src/api/coursesApi.ts → getCourses()
 */
class CourseRepository {

    private val api get() = RetrofitClient.courseApi

    /** GET /api/courses */
    suspend fun getCourses(): List<CourseDto> {
        val response = api.getCourses()
        return response.body() ?: emptyList()
    }
}