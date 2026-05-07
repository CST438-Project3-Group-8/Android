package com.example.studyhive_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyhive_android.data.model.CourseDto
import com.example.studyhive_android.data.model.UpdateProfileRequest
import com.example.studyhive_android.data.model.UserDto
import com.example.studyhive_android.data.repository.CourseRepository
import com.example.studyhive_android.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for [ProfileScreen].
 * Mirrors ProfilePage.tsx — profile load, edit, course management, delete account.
 */
class ProfileViewModel(
    private val userRepo:   UserRepository   = UserRepository(),
    private val courseRepo: CourseRepository = CourseRepository()
) : ViewModel() {

    data class ProfileUiState(
        val loading: Boolean = true,
        val loadError: String? = null,
        val profile: UserDto? = null,
        val myCourses: List<CourseDto> = emptyList(),
        val allCourses: List<CourseDto> = emptyList(),
        val saving: Boolean = false,
        val saveError: String? = null,
        val saveSuccess: Boolean = false,
        val courseError: String? = null,
        val accountDeleted: Boolean = false
    ) {
        val availableToAdd: List<CourseDto>
            get() {
                val enrolled = myCourses.map { it.id }.toSet()
                return allCourses.filter { it.id !in enrolled }
            }
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(loading = true)
            try {
                val profileDeferred   = async { userRepo.getMyProfile() }
                val myCoursesDeferred = async { userRepo.getMyCourses() }
                val allCoursesDeferred = async { courseRepo.getCourses() }

                _uiState.value = ProfileUiState(
                    loading    = false,
                    profile    = profileDeferred.await(),
                    myCourses  = myCoursesDeferred.await(),
                    allCourses = allCoursesDeferred.await()
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    loading   = false,
                    loadError = e.message ?: "Failed to load profile."
                )
            }
        }
    }

    fun saveProfile(name: String, bio: String, major: String) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(saveError = "Full name is required.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(saving = true, saveError = null, saveSuccess = false)
            try {
                val updated = userRepo.updateProfile(
                    UpdateProfileRequest(
                        name  = name.trim(),
                        bio   = bio.trim(),
                        major = major.trim()
                    )
                )
                _uiState.value = _uiState.value.copy(
                    saving      = false,
                    profile     = updated,
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    saving    = false,
                    saveError = e.message ?: "Failed to save profile."
                )
            }
        }
    }

    fun addCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                userRepo.addCourse(courseId)
                val added = _uiState.value.allCourses.find { it.id == courseId }
                if (added != null) {
                    _uiState.value = _uiState.value.copy(
                        myCourses = _uiState.value.myCourses + added,
                        courseError = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    courseError = e.message ?: "Failed to add course."
                )
            }
        }
    }

    fun removeCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                userRepo.removeCourse(courseId)
                _uiState.value = _uiState.value.copy(
                    myCourses   = _uiState.value.myCourses.filter { it.id != courseId },
                    courseError = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    courseError = e.message ?: "Failed to remove course."
                )
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                userRepo.deleteAccount()
                _uiState.value = _uiState.value.copy(accountDeleted = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    saveError = e.message ?: "Failed to delete account."
                )
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}