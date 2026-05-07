package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyhive_android.ui.viewmodels.AuthViewModel

/**
 * Login screen — wired to [AuthViewModel].
 *
 * Handles:
 *  - Email + password sign-in  (calls Supabase directly via AuthViewModel)
 *  - Google OAuth              (opens browser, deep-link returns to MainActivity)
 *  - GitHub OAuth              (same pattern)
 */
@Composable
fun LoginScreen(
    onShowSignup: () -> Unit,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val actionError by authViewModel.actionError.collectAsStateWithLifecycle()
    val authState   by authViewModel.authState.collectAsStateWithLifecycle()

    // Navigate when authenticated
    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Authenticated) {
            onLoginSuccess()
        }
    }

    val isLoading = authState is AuthViewModel.AuthState.Bootstrapping

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF3F4F6)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Brand
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(48.dp).width(48.dp)
                        .background(Color(0xFF2563EB), RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text("StudyHive", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            }

            Spacer(modifier = Modifier.height(40.dp))
            Text("Welcome Back!", style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to StudyHive?", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF475569))
                TextButton(onClick = onShowSignup) {
                    Text("Create an Account", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google OAuth
            OutlinedButton(
                onClick = { authViewModel.signInWithGoogle() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = !isLoading
            ) {
                Text("G", color = Color(0xFFEA4335), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Sign in with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GitHub OAuth
            OutlinedButton(
                onClick = { authViewModel.signInWithGitHub() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = !isLoading
            ) {
                Text("⌘", color = Color(0xFF111827), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Sign in with GitHub")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Or continue with email", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(16.dp))

            // Error banner
            if (actionError != null) {
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF2F2)) {
                    Text(text = actionError ?: "", modifier = Modifier.padding(12.dp),
                        color = Color(0xFFDC2626), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Email field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Email address", style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email, onValueChange = { email = it; authViewModel.clearActionError() },
                    placeholder = { Text("student@university.edu") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Password", style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password, onValueChange = { password = it; authViewModel.clearActionError() },
                    placeholder = { Text("Your password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Hide" else "Show",
                                style = MaterialTheme.typography.bodySmall, color = Color(0xFF2563EB))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.clearActionError()
                    authViewModel.signInWithEmail(email.trim(), password)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp),
                        color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Sign In", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}