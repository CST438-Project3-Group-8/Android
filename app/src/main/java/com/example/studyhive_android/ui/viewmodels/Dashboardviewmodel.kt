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
 * ViewModel for [DashboardScreen].
 * Mirrors DashboardPage.tsx — loads groups, courses, and upcoming sessions.
 */
class DashboardViewModel(
    private val groupRepo: GroupRepository = GroupRepository(),
    private val courseRepo: CourseRepository = CourseRepository(),
    private val sessionRepo: SessionRepository = SessionRepository()
) : ViewModel() {

    data class DashboardUiState(
        val loading: Boolean = true,
        val error: String? = null,
        val groups: List<StudyGroupDto> = emptyList(),
        val upcomingSessions: List<Pair<SessionDto, StudyGroupDto>> = emptyList(),
        val totalCourses: Int = 0,
        val courseMap: Map<Int, String> = emptyMap()
    )

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun load(currentUserId: String?) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(loading = true)
            try {
                val groupsDeferred  = async { groupRepo.getGroups() }
                val joinedDeferred  = async { groupRepo.getMyJoinedGroups() }
                val coursesDeferred = async { courseRepo.getCourses() }

                val allGroups = groupsDeferred.await()
                val joinedGroups = joinedDeferred.await()
                val courses   = coursesDeferred.await()

                val courseMap = courses.associate { it.id to it.code }
                val owned     = allGroups.filter { it.creatorId == currentUserId }
                val dashboardGroups = (owned + joinedGroups).distinctBy { it.id }

                // Fetch sessions for the user's groups in parallel
                val sessionPairs = dashboardGroups.flatMap { group ->
                    try {
                        sessionRepo.getSessionsByGroup(group.id)
                            .map { session -> session to group }
                    } catch (e: Exception) {
                        emptyList()
                    }
                }.sortedBy { (session, _) -> session.scheduledAt }

                _uiState.value = DashboardUiState(
                    loading          = false,
                    groups           = dashboardGroups,
                    upcomingSessions = sessionPairs.take(4),
                    totalCourses     = courses.size,
                    courseMap        = courseMap
                )
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    loading = false,
                    error   = e.message ?: "Unable to load dashboard."
                )
            }
        }
    }
}