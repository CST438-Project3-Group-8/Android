package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.*

import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("api/user/me")
    suspend fun getMyProfile(): Response<UserDto>

    @POST("api/user")
    suspend fun createOrBootstrapProfile(): Response<UserDto>

    @PUT("api/user")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserDto>

    @GET("api/user/me/courses")
    suspend fun getMyCourses(): Response<List<CourseDto>>

    @POST("api/user/me/courses/{courseId}")
    suspend fun addMyCourse(@Path("courseId") courseId: Int): Response<Unit>

    @DELETE("api/user/me/courses/{courseId}")
    suspend fun removeMyCourse(@Path("courseId") courseId: Int): Response<Unit>

    @DELETE("api/user/me")
    suspend fun deleteMyAccount(): Response<Unit>
}
