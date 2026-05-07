package com.example.studyhive_android.data.network

import com.example.studyhive_android.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {

    @GET("api/groups")
    suspend fun getGroups(): Response<List<StudyGroupDto>>

    @GET("api/groups/{id}")
    suspend fun getGroupById(@Path("id") id: Int): Response<StudyGroupDto>

    @POST("api/groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<StudyGroupDto>

    @DELETE("api/groups/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): Response<Unit>

    // ── Added: join / leave / membership / my-groups ──────────────────────

    @POST("api/groups/{groupId}/join")
    suspend fun joinGroup(@Path("groupId") groupId: Int): Response<Unit>

    @DELETE("api/groups/{groupId}/leave")
    suspend fun leaveGroup(@Path("groupId") groupId: Int): Response<Unit>

    @GET("api/groups/{groupId}/membership")
    suspend fun getMembership(@Path("groupId") groupId: Int): Response<Map<String, Any>>

    @GET("api/groups/me/joined")
    suspend fun getMyJoinedGroups(): Response<List<StudyGroupDto>>
}