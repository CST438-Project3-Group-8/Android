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

class BrowseGroupsViewModel(
    private val groupRepo: GroupRepository = GroupRepository(),
    private val courseRepo: CourseRepository = CourseRepository()
) : ViewModel() {

    data class BrowseUiState(
        val loading: Boolean = true,
        val error: String? = null,
        val allGroups: List<StudyGroupDto> = emptyList(),
        val courseMap: Map<Int, String> = emptyMap(),
        val searchQuery: String = "",
        val selectedFilter: String = "All",
        val joiningGroupId: Int? = null,
        val actionMessage: String? = null
    ) {
        val filtered: List<StudyGroupDto>
            get() = allGroups.filter { group ->
                val courseCode = courseMap[group.courseId].orEmpty()

                val matchesSearch = searchQuery.isBlank() ||
                    group.title.orEmpty().contains(searchQuery, ignoreCase = true) ||
                    group.description.orEmpty().contains(searchQuery, ignoreCase = true) ||
                    courseCode.contains(searchQuery, ignoreCase = true)

                val matchesFilter = selectedFilter == "All" ||
                    group.meetingMode.orEmpty().equals(selectedFilter, ignoreCase = true) ||
                    courseCode.equals(selectedFilter, ignoreCase = true)

                matchesSearch && matchesFilter
            }
    }

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val groups = groupRepo.getGroups()
                val courses = courseRepo.getCourses()

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    allGroups = groups,
                    courseMap = courses.associate { it.id to it.code }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Unable to load groups. Is the backend running?"
                )
            }
        }
    }

    fun setSearch(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun joinGroup(groupId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(joiningGroupId = groupId, actionMessage = null)
            groupRepo.joinGroup(groupId)
                .onSuccess {
                    load()
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage = "Joined group!"
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage = e.message ?: "Could not join group."
                    )
                }
        }
    }

    fun leaveGroup(groupId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(joiningGroupId = groupId, actionMessage = null)
            groupRepo.leaveGroup(groupId)
                .onSuccess {
                    load()
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage = "Left group."
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        joiningGroupId = null,
                        actionMessage = e.message ?: "Could not leave group."
                    )
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }
}
