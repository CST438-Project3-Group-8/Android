package com.example.studyhive_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.studyhive_android.ui.screens.DashboardScreen
import com.example.studyhive_android.ui.screens.LoginScreen
import com.example.studyhive_android.ui.screens.SessionUi
import com.example.studyhive_android.ui.screens.SignupScreen
import com.example.studyhive_android.ui.theme.StudyHiveAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyHiveAndroidTheme {
                var currentScreen by rememberSaveable { mutableStateOf("login") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF3F4F6)
                ) {
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

                        else -> {
                            DashboardScreen(
                                userName = "Alex",
                                upcomingSessions = listOf(
                                    SessionUi(
                                        title = "Dynamic Programming Review",
                                        subtitle = "Algorithm Enthusiasts",
                                        timeLabel = "TODAY 4:00",
                                        location = "Main Library, Room 402"
                                    ),
                                    SessionUi(
                                        title = "Midterm 2 Practice Exam",
                                        subtitle = "Calculus III Prep",
                                        timeLabel = "TOMORROW 7:00",
                                        location = "Zoom"
                                    )
                                ),
                                onLogout = { currentScreen = "login" }
                            )
                        }
                    }
                }
            }
        }
    }
}