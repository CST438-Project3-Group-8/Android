package com.example.studyhive_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyhive_android.data.model.SessionDto
import com.example.studyhive_android.data.model.StudyGroupDto
import com.example.studyhive_android.data.repository.CourseRepository
import com.example.studyhive_android.data.repository.GroupRepository
import com.example.studyhive_android.data.repository.SessionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for [MyGroupsScreen].
 * Mirrors MyGroupsPage.tsx — shows groups the user owns or has joined, each with its next session.
 */
class MyGroupsViewModel(
    private val groupRepo:   GroupRepository   = GroupRepository(),
    private val courseRepo:  CourseRepository  = CourseRepository(),
    private val sessionRepo: SessionRepository = SessionRepository()
) : ViewModel() {

    data class MyGroupsUiState(
        val loading: Boolean = true,
        val error: String? = null,
        val groups: List<StudyGroupDto> = emptyList(),
        val courseMap: Map<Int, String> = emptyMap(),
        val nextSessionMap: Map<Int, SessionDto?> = emptyMap()
    )

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    fun load(currentUserId: String?) {
        viewModelScope.launch {
            _uiState.value = MyGroupsUiState(loading = true)
            try {
                val groupsDeferred  = async { groupRepo.getGroups() }
                val joinedDeferred  = async { groupRepo.getMyJoinedGroups() }
                val coursesDeferred = async { courseRepo.getCourses() }

                val allGroups = groupsDeferred.await()
                val joinedGroups = joinedDeferred.await()
                val courses   = coursesDeferred.await()
                val owned     = allGroups.filter { it.creatorId == currentUserId }
                val myGroups  = (owned + joinedGroups).distinctBy { it.id }

                val nextMap = myGroups.associate { group ->
                    val next = try {
                        sessionRepo.getSessionsByGroup(group.id)
                            .sortedBy { it.scheduledAt }
                            .firstOrNull()
                    } catch (e: Exception) { null }
                    group.id to next
                }

                _uiState.value = MyGroupsUiState(
                    loading       = false,
                    groups        = myGroups,
                    courseMap     = courses.associate { it.id to it.code },
                    nextSessionMap = nextMap
                )
            } catch (e: Exception) {
                _uiState.value = MyGroupsUiState(
                    loading = false,
                    error   = e.message ?: "Unable to load your groups."
                )
            }
        }
    }
}