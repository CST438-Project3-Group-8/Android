package com.example.studyhive_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyhive_android.data.repository.AuthRepository
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Auth ViewModel — Android equivalent of [AuthContext.tsx].
 *
 * Holds:
 *  - [authState]  : current auth + bootstrap state
 *  - sign-in / sign-up / sign-out actions
 *
 * After every successful Supabase sign-in, this ViewModel calls
 * [AuthRepository.bootstrapBackendProfile] to upsert the user record on the
 * Spring Boot backend — exactly what [AuthContext.tsx] does with createProfile().
 */
class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // ── State ────────────────────────────────────────────────────────────

    sealed class AuthState {
        object Loading : AuthState()
        object Unauthenticated : AuthState()
        data class Bootstrapping(val session: UserSession) : AuthState()
        data class Authenticated(val session: UserSession, val displayName: String, val email: String) : AuthState()
        data class BootstrapError(val session: UserSession, val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    private var authStateVersion = 0

    init {
        observeSession()
    }

    // ── Session observer ─────────────────────────────────────────────────

    private fun observeSession() {
        viewModelScope.launch {
            repo.sessionFlow.collect { session ->
                val version = ++authStateVersion
                when {
                    session == null -> _authState.value = AuthState.Unauthenticated
                    else            -> bootstrapWithSession(session, version)
                }
            }
        }
    }

    private suspend fun bootstrapWithSession(session: UserSession, version: Int = authStateVersion) {
        _authState.value = AuthState.Bootstrapping(session)
        try {
            repo.bootstrapBackendProfile()
            if (version != authStateVersion) return
            _authState.value = AuthState.Authenticated(
                session = session,
                displayName = repo.displayName(),
                email = repo.email()
            )
        } catch (e: Exception) {
            if (version != authStateVersion) return
            _authState.value = AuthState.BootstrapError(
                session = session,
                message = e.message ?: "Could not finish signing you in."
            )
        }
    }

    // ── Sign-in actions ─────────────────────────────────────────────────

    fun signInWithGoogle() {
        viewModelScope.launch {
            _actionError.value = null
            runCatching { repo.signInWithGoogle() }
                .onFailure { _actionError.value = it.message }
        }
    }

    fun signInWithGitHub() {
        viewModelScope.launch {
            _actionError.value = null
            runCatching { repo.signInWithGitHub() }
                .onFailure { _actionError.value = it.message }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _actionError.value = null
            runCatching { repo.signInWithEmail(email, password) }
                .onFailure { _actionError.value = it.message }
        }
    }

    fun signUpWithEmail(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            _actionError.value = null
            runCatching { repo.signUpWithEmail(email, password, fullName) }
                .onFailure { _actionError.value = it.message }
        }
    }

    fun signOut() {
        authStateVersion++
        _actionError.value = null
        _authState.value = AuthState.Unauthenticated
        viewModelScope.launch {
            runCatching { repo.signOut() }
                .onFailure { _actionError.value = it.message ?: "Could not sign out." }
        }
    }

    fun retryBootstrap() {
        val current = _authState.value
        if (current is AuthState.BootstrapError) {
            viewModelScope.launch {
                bootstrapWithSession(current.session, authStateVersion)
            }
        }
    }

    fun clearActionError() {
        _actionError.value = null
    }

    // ── Convenience getters ─────────────────────────────────────────────

    val currentUserId: String? get() = repo.currentUserId()
    val displayName:   String  get() = repo.displayName()
    val email: String get() = repo.email()
    val isAuthenticated: Boolean
        get() = _authState.value is AuthState.Authenticated
}