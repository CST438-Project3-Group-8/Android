package com.example.studyhive_android.data.repository

import com.example.studyhive_android.data.model.CreateGroupRequest
import com.example.studyhive_android.data.model.StudyGroupDto
import com.example.studyhive_android.data.network.RetrofitClient

/**
 * Repository for study-group operations.
 *
 * Mirrors studyhive-web/src/api/groupsApi.ts:
 *   getGroups(), getGroupById(), createGroup(), deleteGroup()
 *
 * Also provides the join/leave endpoints that the web calls directly from
 * GroupController on the backend.
 */
class GroupRepository {

    private val api get() = RetrofitClient.groupApi

    /** GET /api/groups */
    suspend fun getGroups(): List<StudyGroupDto> {
        val response = api.getGroups()
        return response.body() ?: emptyList()
    }

    /** GET /api/groups/{id} */
    suspend fun getGroupById(id: Int): StudyGroupDto? {
        val response = api.getGroupById(id)
        return if (response.isSuccessful) response.body() else null
    }

    /** POST /api/groups */
    suspend fun createGroup(request: CreateGroupRequest): StudyGroupDto {
        val response = api.createGroup(request)
        return response.body()
            ?: throw IllegalStateException("Failed to create group: HTTP ${response.code()}")
    }

    /** DELETE /api/groups/{id} */
    suspend fun deleteGroup(id: Int) {
        api.deleteGroup(id)
    }

    /** POST /api/groups/{groupId}/join */
    suspend fun joinGroup(groupId: Int): Result<Unit> = runCatching {
        val response = RetrofitClient.groupApi.joinGroup(groupId)
        if (!response.isSuccessful) {
            throw IllegalStateException("Join failed: HTTP ${response.code()}")
        }
    }

    /** DELETE /api/groups/{groupId}/leave */
    suspend fun leaveGroup(groupId: Int): Result<Unit> = runCatching {
        val response = RetrofitClient.groupApi.leaveGroup(groupId)
        if (!response.isSuccessful) {
            throw IllegalStateException("Leave failed: HTTP ${response.code()}")
        }
    }

    /** GET /api/groups/{groupId}/membership */
    suspend fun getMembership(groupId: Int): Boolean = runCatching {
        val response = RetrofitClient.groupApi.getMembership(groupId)
        response.body()?.get("joined") as? Boolean ?: false
    }.getOrDefault(false)

    /** GET /api/groups/me/joined */
    suspend fun getMyJoinedGroups(): List<StudyGroupDto> = runCatching {
        val response = RetrofitClient.groupApi.getMyJoinedGroups()
        response.body() ?: emptyList()
    }.getOrDefault(emptyList())
}