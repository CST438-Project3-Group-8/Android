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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SessionUi(
    val title: String,
    val subtitle: String,
    val timeLabel: String,
    val location: String
)

data class GroupUi(
    val courseTag: String,
    val modeTag: String,
    val title: String,
    val description: String,
    val memberText: String
)

@Composable
fun DashboardScreen(
    userName: String = "Alex",
    upcomingSessions: List<SessionUi> = emptyList(),
    groups: List<GroupUi> = sampleGroups(),
    onLogout: () -> Unit = {},
    onCreateGroup: () -> Unit = {},
    onFindGroups: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderSection(onLogout = onLogout)
        }

        item {
            WelcomeCard(
                userName = userName,
                sessionCount = upcomingSessions.size,
                onCreateGroup = onCreateGroup,
                onFindGroups = onFindGroups
            )
        }

        item {
            StatsRow(
                activeGroups = groups.size,
                upcomingSessions = upcomingSessions.size,
                coursesEnrolled = 4
            )
        }

        item {
            SectionTitle("Upcoming Sessions")
        }

        if (upcomingSessions.isEmpty()) {
            item { EmptySessionsCard() }
        } else {
            items(upcomingSessions) { session ->
                SessionCard(session)
            }
        }

        item {
            SectionTitle("Your Study Groups")
        }

        items(groups) { group ->
            GroupCard(group)
        }
    }
}

@Composable
private fun HeaderSection(onLogout: () -> Unit) {
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
            Text("Log out", color = Color(0xFF64748B))
        }
    }
}

@Composable
private fun WelcomeCard(
    userName: String,
    sessionCount: Int,
    onCreateGroup: () -> Unit,
    onFindGroups: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Welcome back, $userName!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (sessionCount == 0)
                    "You have no upcoming sessions right now."
                else
                    "You have $sessionCount study sessions coming up.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = onCreateGroup,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Create Group")
                }

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedButton(
                    onClick = onFindGroups,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Find Groups")
                }
            }
        }
    }
}

@Composable
private fun StatsRow(
    activeGroups: Int,
    upcomingSessions: Int,
    coursesEnrolled: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard("Active Groups", activeGroups.toString())
        StatCard("Upcoming Sessions", upcomingSessions.toString())
        StatCard("Courses Enrolled", coursesEnrolled.toString())
    }
}

@Composable
private fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
    )
}

@Composable
private fun EmptySessionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "No sessions ahead",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You do not have any upcoming study sessions yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun SessionCard(session: SessionUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = session.timeLabel,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
            }

            OutlinedButton(onClick = { }) {
                Text("Open")
            }
        }
    }
}

@Composable
private fun GroupCard(group: GroupUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row {
                AssistChip(
                    onClick = { },
                    label = { Text(group.courseTag) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFF1F5F9))
                )

                Spacer(modifier = Modifier.width(8.dp))

                AssistChip(
                    onClick = { },
                    label = { Text(group.modeTag) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFDCFCE7))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = group.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = group.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = group.memberText,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

private fun sampleGroups(): List<GroupUi> {
    return listOf(
        GroupUi(
            courseTag = "CS 301",
            modeTag = "In-Person",
            title = "Algorithm Enthusiasts",
            description = "Weekly review of algorithms, focusing on dynamic programming, graphs, and greedy methods.",
            memberText = "5 / 8 members"
        ),
        GroupUi(
            courseTag = "MATH 220",
            modeTag = "Online",
            title = "Calculus III Prep",
            description = "Preparing for the upcoming midterms. Bring your problem sets and we will work through them together.",
            memberText = "12 / 15 members"
        )
    )
}