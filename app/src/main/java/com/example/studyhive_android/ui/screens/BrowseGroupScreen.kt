package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BrowseGroupScreen(
    onBackClick: () -> Unit,
    onCreateGroup: () -> Unit,
    onProfileClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All", "CS 301", "MATH 220", "PHYS 101", "ENG 101", "Online", "In-Person")
    var selectedFilter by remember { mutableStateOf("All") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top Bar with Back, Search and Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF64748B)
                    )
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search groups...", fontSize = 14.sp) },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Search, 
                            contentDescription = null, 
                            modifier = Modifier.size(20.dp)
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF2563EB)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Discover Groups",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                            
                            Button(
                                onClick = onCreateGroup,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Create", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        Text(
                            text = "Find and join study groups that match your courses.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, RoundedCornerShape(10.dp))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList, 
                                    contentDescription = null, 
                                    tint = Color(0xFF64748B), 
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        items(filters) { filter ->
                            FilterChip(
                                text = filter,
                                isSelected = selectedFilter == filter,
                                onClick = { selectedFilter = filter }
                            )
                        }
                    }
                }

                items(sampleBrowseGroups) { group ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        BrowseGroupCard(group)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFF2563EB) else Color.White,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF64748B)
        )
    }
}

@Composable
private fun BrowseGroupCard(group: BrowseGroupData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Tag(text = group.course, backgroundColor = Color(0xFFF1F5F9), textColor = Color(0xFF475569))
                Tag(
                    text = group.mode,
                    backgroundColor = when(group.mode) {
                        "Online" -> Color(0xFFEFF6FF)
                        "In-Person" -> Color(0xFFF0FDF4)
                        else -> Color(0xFFFAF5FF)
                    },
                    textColor = when(group.mode) {
                        "Online" -> Color(0xFF2563EB)
                        "In-Person" -> Color(0xFF16A34A)
                        else -> Color(0xFF9333EA)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = group.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = group.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(icon = Icons.Default.DateRange, text = group.schedule)
                InfoRow(icon = Icons.Default.LocationOn, text = group.location)
                InfoRow(icon = Icons.Default.Groups, text = "${group.memberCount} / ${group.maxMembers} members")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MemberAvatars(count = group.memberCount)
                
                if (group.isJoined) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF0FDF4),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Joined", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                } else {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("View Details", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
    }
}

@Composable
private fun Tag(text: String, backgroundColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun MemberAvatars(count: Int) {
    Row {
        repeat(minOf(count, 3)) { i ->
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (i * -8).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (count > 3) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (3 * -8).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${count - 3}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

private data class BrowseGroupData(
    val course: String,
    val mode: String,
    val name: String,
    val description: String,
    val schedule: String,
    val location: String,
    val memberCount: Int,
    val maxMembers: Int,
    val isJoined: Boolean
)

private val sampleBrowseGroups = listOf(
    BrowseGroupData(
        course = "CS 301",
        mode = "In-Person",
        name = "Algorithm Enthusiasts",
        description = "Weekly review of algorithms, focusing on dynamic programming, graphs, and greedy algorithms.",
        schedule = "Thursdays 4:00 PM - 6:00 PM",
        location = "Main Library, Room 402",
        memberCount = 5,
        maxMembers = 8,
        isJoined = true
    ),
    BrowseGroupData(
        course = "MATH 220",
        mode = "Online",
        name = "Calculus III Prep",
        description = "Preparing for the upcoming midterms. Bring your problem sets, and we'll work through the toughest derivatives.",
        schedule = "Tuesdays 7:00 PM - 8:30 PM",
        location = "Zoom",
        memberCount = 12,
        maxMembers = 15,
        isJoined = true
    ),
    BrowseGroupData(
        course = "PHYS 101",
        mode = "Hybrid",
        name = "Physics 101 Study Group",
        description = "Reviewing classical mechanics and completing weekly assignments before they are due on Fridays.",
        schedule = "Mondays 3:00 PM - 5:00 PM",
        location = "Science Building, Rm 112 & Discord",
        memberCount = 4,
        maxMembers = 10,
        isJoined = false
    )
)
