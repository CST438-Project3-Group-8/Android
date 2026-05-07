package com.example.studyhive_android.data.repository

import com.example.studyhive_android.data.model.CourseDto
import com.example.studyhive_android.data.model.UpdateProfileRequest
import com.example.studyhive_android.data.model.UserDto
import com.example.studyhive_android.data.network.RetrofitClient

/**
 * Repository for user-profile operations.
 *
 * Mirrors studyhive-web/src/api/userApi.ts:
 *   getMyProfile(), updateProfile(), getMyCourses(),
 *   addMyCourse(), removeMyCourse(), deleteMyAccount()
 */
class UserRepository {

    private val api get() = RetrofitClient.userApi

    /** GET /api/user/me */
    suspend fun getMyProfile(): UserDto? {
        val response = api.getMyProfile()
        return if (response.isSuccessful) response.body() else null
    }

    /** PUT /api/user  — update name, bio, major */
    suspend fun updateProfile(request: UpdateProfileRequest): UserDto {
        val response = api.updateProfile(request)
        return response.body()
            ?: throw IllegalStateException("Failed to update profile: HTTP ${response.code()}")
    }

    /** GET /api/user/me/courses */
    suspend fun getMyCourses(): List<CourseDto> {
        val response = api.getMyCourses()
        return response.body() ?: emptyList()
    }

    /** POST /api/user/me/courses/{courseId} */
    suspend fun addCourse(courseId: Int) {
        api.addMyCourse(courseId)
    }

    /** DELETE /api/user/me/courses/{courseId} */
    suspend fun removeCourse(courseId: Int) {
        api.removeMyCourse(courseId)
    }

    /** DELETE /api/user/me */
    suspend fun deleteAccount() {
        api.deleteMyAccount()
    }
}