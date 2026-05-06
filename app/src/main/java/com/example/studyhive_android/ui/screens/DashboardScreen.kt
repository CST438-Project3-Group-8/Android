package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    userName: String,
    onLogout: () -> Unit,
    onCreateGroup: () -> Unit,
    onBrowseGroups: () -> Unit,
    onMyGroups: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF4F6FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            DashboardHeader(
                onLogout = onLogout
            )

            Spacer(modifier = Modifier.height(20.dp))

            WelcomeCard(userName = userName)

            Spacer(modifier = Modifier.height(20.dp))

            QuickActionsRow(
                onCreateGroup = onCreateGroup,
                onBrowseGroups = onBrowseGroups,
                onMyGroups = onMyGroups
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmptyGroupsCard(
                onCreateGroup = onCreateGroup,
                onBrowseGroups = onBrowseGroups
            )
        }
    }
}

@Composable
private fun DashboardHeader(
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFF2563EB),
                        shape = RoundedCornerShape(14.dp)
                    )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "StudyHive",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onLogout) {
            Text(
                text = "Logout",
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun WelcomeCard(userName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Welcome back, $userName",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You have not joined any study groups yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun QuickActionsRow(
    onCreateGroup: () -> Unit,
    onBrowseGroups: () -> Unit,
    onMyGroups: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onCreateGroup,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Create Group")
        }

        OutlinedButton(
            onClick = onBrowseGroups,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Browse Groups")
        }

        OutlinedButton(
            onClick = onMyGroups,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("My Groups")
        }
    }
}

@Composable
private fun EmptyGroupsCard(
    onCreateGroup: () -> Unit,
    onBrowseGroups: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "No groups yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Text(
                text = "Once you create or join study groups, they will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onCreateGroup,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text("Create First Group")
            }

            OutlinedButton(
                onClick = onBrowseGroups,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Browse Groups")
            }
        }
    }
}