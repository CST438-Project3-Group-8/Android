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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class MyGroupUi(
    val courseTag: String,
    val statusTag: String,
    val modeTag: String,
    val title: String,
    val description: String,
    val membersLabel: String,
    val nextSessionTitle: String,
    val nextSessionTime: String
)

@Composable
fun MyGroupsScreen(
    groups: List<MyGroupUi> = sampleMyGroups(),
    onBackToDashboard: () -> Unit = {},
    onFindMoreGroups: () -> Unit = {}
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
            MyGroupsHeader(
                onBackToDashboard = onBackToDashboard
            )
        }

        item {
            TitleSection(
                onFindMoreGroups = onFindMoreGroups
            )
        }

        items(groups) { group ->
            MyGroupCard(group = group)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MyGroupsHeader(
    onBackToDashboard: () -> Unit
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

        TextButton(onClick = onBackToDashboard) {
            Text(
                text = "Dashboard",
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun TitleSection(
    onFindMoreGroups: () -> Unit
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
                text = "My Groups",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Manage your study groups and upcoming sessions.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onFindMoreGroups,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text("Find More Groups")
            }
        }
    }
}

@Composable
private fun MyGroupCard(
    group: MyGroupUi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(group.courseTag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFF1F5F9)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                AssistChip(
                    onClick = { },
                    label = { Text(group.statusTag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFDCFCE7)
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                AssistChip(
                    onClick = { },
                    label = { Text(group.modeTag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (group.modeTag == "Online") {
                            Color(0xFFDBEAFE)
                        } else {
                            Color(0xFFFFEDD5)
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = group.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = group.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            MembersRow(membersLabel = group.membersLabel)

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            NextSessionCard(
                title = group.nextSessionTitle,
                time = group.nextSessionTime
            )
        }
    }
}

@Composable
private fun MembersRow(
    membersLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarStack()

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = membersLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
private fun AvatarStack() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SmallAvatar(Color(0xFF93C5FD))
        SmallAvatar(Color(0xFFC4B5FD))
        SmallAvatar(Color(0xFFFCA5A5))
        MoreMembersBubble("+2")
    }
}

@Composable
private fun SmallAvatar(color: Color) {
    Box(
        modifier = Modifier
            .padding(end = 6.dp)
            .size(28.dp)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
private fun MoreMembersBubble(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF1F5F9),
                shape = CircleShape
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun NextSessionCard(
    title: String,
    time: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📅",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NEXT SESSION",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }

            Text(
                text = "→",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

private fun sampleMyGroups(): List<MyGroupUi> {
    return listOf(
        MyGroupUi(
            courseTag = "CS 301",
            statusTag = "Active",
            modeTag = "In-Person",
            title = "Algorithm Enthusiasts",
            description = "Weekly review of algorithms, focusing on dynamic programming, graphs, and greedy methods. Great for interview prep!",
            membersLabel = "5 Members",
            nextSessionTitle = "Dynamic Programming Review",
            nextSessionTime = "Today, 4:00 PM · Main Library, Room 402"
        ),
        MyGroupUi(
            courseTag = "MATH 220",
            statusTag = "Active",
            modeTag = "Online",
            title = "Calculus III Prep",
            description = "Preparing for the upcoming midterms. Bring your problem sets, and we'll work through the toughest derivatives together.",
            membersLabel = "12 Members",
            nextSessionTitle = "Midterm 2 Practice Exam",
            nextSessionTime = "Tomorrow, 7:00 PM · Zoom"
        )
    )
}