package com.example.studyhive_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyhive_android.data.model.StudyGroupDto
import com.example.studyhive_android.data.repository.CourseRepository
import com.example.studyhive_android.data.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for [BrowseGroupScreen].
 * Mirrors FindGroupsPage.tsx — loads all groups, supports search + mode filter,
 * and exposes join/leave actions.
 */
class BrowseGroupsViewModel(
    private val groupRepo: GroupRepository = GroupRepository(),
    private val courseRepo: CourseRepository = CourseRepository()
) : ViewModel() {

    data class BrowseUiState(
        val loading: Boolean = true,
        val error: String? = null,
        val allGroups: List<StudyGroupDto> = emptyList(),
        val courseMap: Map<Int, String> = emptyMap(),
        val joinedGroupIds: Set<Int> = emptySet(),
        val searchQuery: String = "",
        val modeFilter: String = "All",
        val joiningGroupId: Int? = null,
        val actionMessage: String? = null
    ) {
        val filtered: List<StudyGroupDto>
            get() {
                return allGroups.filter { group ->
                    val matchSearch = searchQuery.isBlank() ||
                            group.title.orEmpty().contains(searchQuery, ignoreCase = true) ||
                            group.description.orEmpty().contains(searchQuery, ignoreCase = true) ||
                            courseMap[group.courseId].orEmpty().contains(searchQuery, ignoreCase = true)
                    val courseCode = courseMap[group.courseId].orEmpty()
                    val matchFilter = modeFilter == "All" ||
                            group.meetingMode == modeFilter ||
                            courseCode == modeFilter
                    matchSearch && matchFilter
                }
            }
    }

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val groups  = groupRepo.getGroups()
                val joinedGroups = groupRepo.getMyJoinedGroups()
                val courses = courseRepo.getCourses()
                _uiState.value = _uiState.value.copy(
                    loading   = false,
                    allGroups = groups,
                    courseMap = courses.associate { it.id to it.code },
                    joinedGroupIds = joinedGroups.map { it.id }.toSet()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error   = e.message ?: "Unable to load groups."
                )
            }
        }
    }

    fun setSearch(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setModeFilter(filter: String) {
        _uiState.value = _uiState.value.copy(modeFilter = filter)
    }

    fun joinGroup(groupId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(joiningGroupId = groupId, actionMessage = null)
            groupRepo.joinGroup(groupId)
                .onSuccess {
                    val joinedIds = _uiState.value.joinedGroupIds + groupId
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        joinedGroupIds = joinedIds,
                        actionMessage  = "Joined group!"
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage  = e.message ?: "Could not join group."
                    )
                }
        }
    }

    fun leaveGroup(groupId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(joiningGroupId = groupId, actionMessage = null)
            groupRepo.leaveGroup(groupId)
                .onSuccess {
                    val joinedIds = _uiState.value.joinedGroupIds - groupId
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        joinedGroupIds = joinedIds,
                        actionMessage  = "Left group."
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage  = e.message ?: "Could not leave group."
                    )
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }
}