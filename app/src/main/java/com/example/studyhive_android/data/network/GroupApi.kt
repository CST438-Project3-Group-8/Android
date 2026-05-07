package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.*

import retrofit2.Response
import retrofit2.http.*

// ── GroupApi.kt ───────────────────────────────────────────────────────────────
interface GroupApi {
    @GET("api/groups")
    suspend fun getGroups(): Response<List<StudyGroupDto>>

    @GET("api/groups/{id}")
    suspend fun getGroupById(@Path("id") id: Int): Response<StudyGroupDto>

    @POST("api/groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<StudyGroupDto>

    @DELETE("api/groups/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): Response<Unit>
}
