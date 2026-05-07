package com.example.studyhive_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyhive_android.ui.screens.BrowseGroupScreen
import com.example.studyhive_android.ui.screens.CreateGroupScreen
import com.example.studyhive_android.ui.screens.DashboardScreen
import com.example.studyhive_android.ui.screens.LoginScreen
import com.example.studyhive_android.ui.screens.MyGroupsScreen
import com.example.studyhive_android.ui.screens.ProfileScreen
import com.example.studyhive_android.ui.screens.ResetPasswordScreen
import com.example.studyhive_android.ui.screens.SignupScreen
import com.example.studyhive_android.ui.theme.StudyHiveAndroidTheme
import com.example.studyhive_android.ui.viewmodels.ProfileViewModel

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
    val backStack = remember { mutableStateListOf<String>() }

    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.uiState.collectAsState()

    fun navigate(route: String) {
        backStack.add(currentScreen)
        currentScreen = route
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }

    LaunchedEffect(currentScreen) {
        if (currentScreen == Routes.DASHBOARD || currentScreen == Routes.PROFILE) {
            profileViewModel.load()
        }
    }

    when (currentScreen) {
        Routes.LOGIN -> LoginScreen(
            onShowSignup = { navigate(Routes.SIGNUP) },
            onLoginSuccess = {
                backStack.clear()
                currentScreen = Routes.DASHBOARD
                profileViewModel.load()
            }
        )

        Routes.SIGNUP -> SignupScreen(
            onShowLogin = { navigateBack() },
            onSignupSuccess = {
                backStack.clear()
                currentScreen = Routes.DASHBOARD
                profileViewModel.load()
            }
        )

        Routes.DASHBOARD -> DashboardScreen(
            userName = profileState.profile?.name
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?.substringBefore(" ")
                ?: "Student",
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
            viewModel = profileViewModel,
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
            onLoginSuccess = {
                backStack.clear()
                currentScreen = Routes.DASHBOARD
                profileViewModel.load()
            }
        )
    }
}
