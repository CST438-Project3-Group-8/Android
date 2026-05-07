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

@Composable
fun SignupScreen(
    onShowLogin: () -> Unit,
    onSignupSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email    by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var localError by rememberSaveable { mutableStateOf("") }

    val actionError by authViewModel.actionError.collectAsStateWithLifecycle()
    val authState   by authViewModel.authState.collectAsStateWithLifecycle()
    val isLoading = authState is AuthViewModel.AuthState.Bootstrapping

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Authenticated) onSignupSuccess()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF3F4F6)) {
        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding().navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.height(48.dp).width(48.dp)
                    .background(Color(0xFF2563EB), RoundedCornerShape(16.dp)))
                Spacer(modifier = Modifier.width(14.dp))
                Text("StudyHive", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text("Create Account", style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?", color = Color(0xFF475569))
                TextButton(onClick = onShowLogin) {
                    Text("Sign In", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = { authViewModel.signInWithGoogle() },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(20.dp),
                enabled = !isLoading) {
                Text("G", color = Color(0xFFEA4335), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Continue with Google")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { authViewModel.signInWithGitHub() },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(20.dp),
                enabled = !isLoading) {
                Text("⌘", color = Color(0xFF111827), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Continue with GitHub")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Or create an account with email", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(16.dp))

            val errorText = actionError ?: localError.takeIf { it.isNotEmpty() }
            if (errorText != null) {
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF2F2)) {
                    Text(errorText, modifier = Modifier.padding(12.dp),
                        color = Color(0xFFDC2626), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            FormLabel("Full name")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = fullName,
                onValueChange = { fullName = it; localError = ""; authViewModel.clearActionError() },
                placeholder = { Text("John Smith") },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), singleLine = true)

            Spacer(modifier = Modifier.height(16.dp))
            FormLabel("Email address")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email,
                onValueChange = { email = it; localError = ""; authViewModel.clearActionError() },
                placeholder = { Text("student@university.edu") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), singleLine = true)

            Spacer(modifier = Modifier.height(16.dp))
            FormLabel("Password")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password,
                onValueChange = { password = it; localError = ""; authViewModel.clearActionError() },
                placeholder = { Text("At least 6 characters") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Hide" else "Show",
                            style = MaterialTheme.typography.bodySmall, color = Color(0xFF2563EB))
                    }
                },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), singleLine = true)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    localError = ""
                    authViewModel.clearActionError()
                    when {
                        fullName.isBlank()   -> localError = "Please enter your full name."
                        email.isBlank()      -> localError = "Please enter your email address."
                        password.length < 6  -> localError = "Password must be at least 6 characters."
                        else -> authViewModel.signUpWithEmail(email.trim(), password, fullName.trim())
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp),
                    color = Color.White, strokeWidth = 2.dp)
                else Text("Create Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(text = text, modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
}