package com.example.studyhive_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.studyhive_android.ui.screens.*
import com.example.studyhive_android.ui.theme.StudyHiveAndroidTheme

// ── Screen route constants ─────────────────────────────────────────────────
private object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val PROFILE = "profile"
    const val RESET_PASSWORD = "resetPassword"
    const val CREATE_GROUP = "createGroup"
    const val BROWSE_GROUPS = "browseGroups"
    const val MY_GROUPS = "myGroups"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyHiveAndroidTheme {
                StudyHiveApp()
            }
        }
    }
}

@Composable
fun StudyHiveApp() {
    var currentScreen by rememberSaveable { mutableStateOf(Routes.LOGIN) }
    // A minimal back stack for back navigation
    val backStack = remember { mutableStateListOf<String>() }

    fun navigate(route: String) {
        backStack.add(currentScreen)
        currentScreen = route
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }

    when (currentScreen) {
        Routes.LOGIN -> LoginScreen(
            onShowSignup = { navigate(Routes.SIGNUP) },
            onLoginSuccess = {
                backStack.clear()
                currentScreen = Routes.DASHBOARD
            }
        )

        Routes.SIGNUP -> SignupScreen(
            onShowLogin = { navigateBack() },
            onSignupSuccess = {
                backStack.clear()
                currentScreen = Routes.DASHBOARD
            }
        )

        Routes.DASHBOARD -> DashboardScreen(
            userName = "Alex",
            onLogout = {
                backStack.clear()
                currentScreen = Routes.LOGIN
            },
            onCreateGroup = { navigate(Routes.CREATE_GROUP) },
            onBrowseGroups = { navigate(Routes.BROWSE_GROUPS) },
            onMyGroups = { navigate(Routes.MY_GROUPS) },
            onProfileClick = { navigate(Routes.PROFILE) }
        )

        Routes.PROFILE -> ProfileScreen(
            onBack = { navigateBack() },
            onSecurityClick = { navigate(Routes.RESET_PASSWORD) }
        )

        Routes.RESET_PASSWORD -> ResetPasswordScreen(
            onBack = { navigateBack() },
            onResetSuccess = { navigateBack() }
        )

        Routes.CREATE_GROUP -> CreateGroupScreen(
            onBack = { navigateBack() },
            onCancel = { navigateBack() },
            onCreateGroup = { navigateBack() }
        )

        Routes.BROWSE_GROUPS -> BrowseGroupScreen(
            onBackClick = { navigateBack() },
            onCreateGroup = { navigate(Routes.CREATE_GROUP) },
            onProfileClick = { navigate(Routes.PROFILE) }
        )

        Routes.MY_GROUPS -> MyGroupsScreen(
            onBackToDashboard = { navigateBack() },
            onFindMoreGroups = { navigate(Routes.BROWSE_GROUPS) },
            onProfileClick = { navigate(Routes.PROFILE) }
        )

        else -> LoginScreen(
            onShowSignup = { navigate(Routes.SIGNUP) },
            onLoginSuccess = { currentScreen = Routes.DASHBOARD }
        )
    }
}