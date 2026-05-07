package com.example.studyhive_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyhive_android.data.model.CourseDto
import com.example.studyhive_android.data.model.CreateGroupRequest
import com.example.studyhive_android.data.repository.CourseRepository
import com.example.studyhive_android.data.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for [CreateGroupScreen].
 * Mirrors CreateGroupPage.tsx.
 */
class CreateGroupViewModel(
    private val groupRepo:  GroupRepository  = GroupRepository(),
    private val courseRepo: CourseRepository = CourseRepository()
) : ViewModel() {

    data class CreateGroupUiState(
        val courses: List<CourseDto> = emptyList(),
        val loadingCourses: Boolean = true,
        val submitting: Boolean = false,
        val error: String? = null,
        val createdGroupId: Int? = null
    )

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            try {
                val courses = courseRepo.getCourses()
                _uiState.value = _uiState.value.copy(courses = courses, loadingCourses = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingCourses = false,
                    error = e.message ?: "Unable to load courses."
                )
            }
        }
    }

    fun createGroup(
        title: String,
        description: String,
        courseId: Int?,
        location: String,
        meetingMode: String,
        maxMembers: Int?
    ) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Group title is required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(submitting = true, error = null)
            try {
                val request = CreateGroupRequest(
                    title       = title.trim(),
                    description = description.trim().ifBlank { null },
                    courseId    = courseId,
                    location    = location.trim().ifBlank { null },
                    meetingMode = meetingMode.ifBlank { null },
                    maxMembers  = maxMembers
                )
                val created = groupRepo.createGroup(request)
                _uiState.value = _uiState.value.copy(
                    submitting     = false,
                    createdGroupId = created.id
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    submitting = false,
                    error      = e.message ?: "Unable to create group."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}