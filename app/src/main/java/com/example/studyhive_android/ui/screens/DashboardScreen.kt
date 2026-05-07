package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyhive_android.ui.viewmodels.DashboardViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun DashboardScreen(
    userName: String,
    currentUserId: String?,
    onLogout: () -> Unit,
    onCreateGroup: () -> Unit,
    onBrowseGroups: () -> Unit,
    onMyGroups: () -> Unit,
    onProfileClick: () -> Unit,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val uiState = dashboardViewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(currentUserId) {
        dashboardViewModel.load(currentUserId)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF2563EB), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("StudyHive", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onLogout) {
                    Text("Logout", color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Welcome card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Welcome back, $userName!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "You have ${uiState.upcomingSessions.size} study sessions coming up across your groups.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onCreateGroup,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+ Create Group", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onBrowseGroups,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Browse Groups")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.loading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error?.let { error ->
                ErrorCard(message = error)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Stat cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(modifier = Modifier.weight(1f), label = "Groups", value = uiState.groups.size.toString(), bg = Color(0xFFDBEAFE))
                StatCard(modifier = Modifier.weight(1f), label = "Sessions", value = uiState.upcomingSessions.size.toString(), bg = Color(0xFFFEF3C7))
                StatCard(modifier = Modifier.weight(1f), label = "Courses", value = uiState.totalCourses.toString(), bg = Color(0xFFDCFCE7))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Upcoming sessions
            SectionHeader(title = "Upcoming Sessions", actionLabel = "View My Groups", onAction = onMyGroups)
            Spacer(modifier = Modifier.height(12.dp))

            if (!uiState.loading && uiState.upcomingSessions.isEmpty()) {
                EmptyCard("No upcoming sessions yet.")
            } else {
                uiState.upcomingSessions.forEachIndexed { index, (session, group) ->
                    if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                    UpcomingSessionCard(
                        time = formatDateTime(session.scheduledAt),
                        title = session.title,
                        group = group.title.orEmpty().ifBlank { "Untitled Group" },
                        location = session.location.orEmpty().ifBlank { group.location.orEmpty().ifBlank { "Location TBD" } }
                    ) { onMyGroups() }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Your groups
            SectionHeader(title = "Your Study Groups", actionLabel = "View All", onAction = onMyGroups)
            Spacer(modifier = Modifier.height(12.dp))

            if (!uiState.loading && uiState.groups.isEmpty()) {
                EmptyCard("You have not joined or created any groups yet.")
            } else {
                uiState.groups.take(3).forEachIndexed { index, group ->
                    if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                    DashboardGroupCard(
                        course = uiState.courseMap[group.courseId].orEmpty().ifBlank { "No Course" },
                        mode = group.meetingMode.orEmpty().ifBlank { "Mode TBD" },
                        title = group.title.orEmpty().ifBlank { "Untitled Group" },
                        description = group.description.orEmpty().ifBlank { "No description provided." },
                        onClick = onMyGroups
                    )
                }
            }
        }
    }
}

private fun formatDateTime(value: String): String {
    return try {
        OffsetDateTime.parse(value).format(DateTimeFormatter.ofPattern("MMM d, h:mm a"))
    } catch (_: DateTimeParseException) {
        value
    }
}

@Composable
private fun ErrorCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFEF2F2)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = Color(0xFFB91C1C),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun EmptyCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF64748B),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, bg: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(bg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionLabel: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        TextButton(onClick = onAction) {
            Text(actionLabel, color = Color(0xFF2563EB), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun UpcomingSessionCard(
    time: String,
    title: String,
    group: String,
    location: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(10.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(time, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), lineHeight = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), style = MaterialTheme.typography.bodyMedium)
                Text(group, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                Text(location, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            }
        }
    }
}

@Composable
private fun DashboardGroupCard(course: String, mode: String, title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagBadge(text = course, bg = Color(0xFFEFF6FF), textColor = Color(0xFF1D4ED8))
                TagBadge(text = mode, bg = Color(0xFFF1F5F9), textColor = Color(0xFF475569))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B), maxLines = 2)
        }
    }
}

@Composable
fun TagBadge(text: String, bg: Color, textColor: Color) {
    Surface(shape = RoundedCornerShape(6.dp), color = bg) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}