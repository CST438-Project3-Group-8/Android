package com.example.studyhive_android.data.model

data class StudyGroupDto(
    val id: Int,
    val title: String?,
    val description: String?,
    val courseId: Int?,
    val creatorId: String?,
    val location: String?,
    val meetingMode: String?,
    val maxMembers: Int?,
    val createdAt: String?
)

data class CreateGroupRequest(
    val title: String,
    val description: String? = null,
    val courseId: Int? = null,
    val location: String? = null,
    val meetingMode: String? = null,
    val maxMembers: Int? = null
)