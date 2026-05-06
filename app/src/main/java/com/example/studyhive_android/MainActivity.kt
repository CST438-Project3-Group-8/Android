package com.example.studyhive_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.studyhive_android.ui.screens.BrowseGroupScreen
import com.example.studyhive_android.ui.screens.CreateGroupScreen
import com.example.studyhive_android.ui.screens.DashboardScreen
import com.example.studyhive_android.ui.screens.LoginScreen
import com.example.studyhive_android.ui.screens.MyGroupsScreen
import com.example.studyhive_android.ui.screens.SignupScreen
import com.example.studyhive_android.ui.theme.StudyHiveAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StudyHiveAndroidTheme {
                var currentScreen by rememberSaveable { mutableStateOf("login") }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onShowSignup = { currentScreen = "signup" },
                            onLoginSuccess = { currentScreen = "dashboard" }
                        )
                    }

                    "signup" -> {
                        SignupScreen(
                            onShowLogin = { currentScreen = "login" },
                            onSignupSuccess = { currentScreen = "dashboard" }
                        )
                    }

                    "dashboard" -> {
                        DashboardScreen(
                            userName = "Alex",
                            onLogout = { currentScreen = "login" },
                            onCreateGroup = { currentScreen = "createGroup" },
                            onBrowseGroups = { currentScreen = "browseGroups" },
                            onMyGroups = { currentScreen = "myGroups" }
                        )
                    }

                    "createGroup" -> {
                        CreateGroupScreen(
                            onBack = { currentScreen = "dashboard" },
                            onCancel = { currentScreen = "dashboard" },
                            onCreateGroup = { currentScreen = "dashboard" }
                        )
                    }

                    "browseGroups" -> {
                        BrowseGroupScreen(
                            onBackClick = { currentScreen = "dashboard" }
                        )
                    }

                    "myGroups" -> {
                        MyGroupsScreen(
                            onBackToDashboard = { currentScreen = "dashboard" },
                            onFindMoreGroups = { currentScreen = "browseGroups" }
                        )
                    }

                    else -> {
                        LoginScreen(
                            onShowSignup = { currentScreen = "signup" },
                            onLoginSuccess = { currentScreen = "dashboard" }
                        )
                    }
                }
            }
        }
    }
}