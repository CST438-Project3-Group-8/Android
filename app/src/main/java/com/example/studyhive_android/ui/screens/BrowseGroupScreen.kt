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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyhive_android.data.model.StudyGroupDto
import com.example.studyhive_android.ui.viewmodels.BrowseGroupsViewModel

@Composable
fun BrowseGroupScreen(
    onBackClick: () -> Unit,
    onCreateGroup: () -> Unit,
    onProfileClick: () -> Unit,
    browseGroupsViewModel: BrowseGroupsViewModel = viewModel()
) {
    val uiState = browseGroupsViewModel.uiState.collectAsStateWithLifecycle().value
    val filters = remember(uiState.allGroups, uiState.courseMap) {
        val modes = uiState.allGroups.mapNotNull { it.meetingMode?.takeIf(String::isNotBlank) }.distinct().sorted()
        val courses = uiState.courseMap.values.distinct().sorted()
        listOf("All") + courses + modes
    }

    LaunchedEffect(Unit) {
        browseGroupsViewModel.load()
    }

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
                    value = uiState.searchQuery,
                    onValueChange = browseGroupsViewModel::setSearch,
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
                                isSelected = uiState.modeFilter == filter,
                                onClick = { browseGroupsViewModel.setModeFilter(filter) }
                            )
                        }
                    }
                }

                if (uiState.loading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                uiState.error?.let { error ->
                    item {
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                            ErrorCard(message = error)
                        }
                    }
                }

                uiState.actionMessage?.let { message ->
                    item {
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                            InfoMessageCard(message = message, onDismiss = browseGroupsViewModel::clearMessage)
                        }
                    }
                }

                if (!uiState.loading && uiState.filtered.isEmpty()) {
                    item {
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                            EmptyBrowseCard("No groups match your search.")
                        }
                    }
                }

                items(uiState.filtered, key = { it.id }) { group ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        BrowseGroupCard(
                            group = group,
                            course = uiState.courseMap[group.courseId].orEmpty().ifBlank { "No Course" },
                            isJoined = group.id in uiState.joinedGroupIds,
                            isBusy = uiState.joiningGroupId == group.id,
                            onJoin = { browseGroupsViewModel.joinGroup(group.id) },
                            onLeave = { browseGroupsViewModel.leaveGroup(group.id) }
                        )
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
private fun BrowseGroupCard(
    group: StudyGroupDto,
    course: String,
    isJoined: Boolean,
    isBusy: Boolean,
    onJoin: () -> Unit,
    onLeave: () -> Unit
) {
    val mode = group.meetingMode.orEmpty().ifBlank { "Mode TBD" }
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
                Tag(text = course, backgroundColor = Color(0xFFF1F5F9), textColor = Color(0xFF475569))
                Tag(
                    text = mode,
                    backgroundColor = when(mode) {
                        "Online" -> Color(0xFFEFF6FF)
                        "In-Person" -> Color(0xFFF0FDF4)
                        else -> Color(0xFFFAF5FF)
                    },
                    textColor = when(mode) {
                        "Online" -> Color(0xFF2563EB)
                        "In-Person" -> Color(0xFF16A34A)
                        else -> Color(0xFF9333EA)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = group.title.orEmpty().ifBlank { "Untitled Group" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = group.description.orEmpty().ifBlank { "No description provided." },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(icon = Icons.Default.DateRange, text = "Schedule TBD")
                InfoRow(icon = Icons.Default.LocationOn, text = group.location.orEmpty().ifBlank { "Location TBD" })
                InfoRow(icon = Icons.Default.Groups, text = "Max ${group.maxMembers ?: "unlimited"} members")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MemberAvatars(count = 0)
                
                if (isJoined) {
                    Surface(
                        modifier = Modifier.clickable(enabled = !isBusy) { onLeave() },
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
                            Text(if (isBusy) "Leaving..." else "Joined", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                } else {
                    Button(
                        onClick = onJoin,
                        enabled = !isBusy,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(if (isBusy) "Joining..." else "Join", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
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
private fun InfoMessageCard(message: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onDismiss() },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEFF6FF)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = Color(0xFF1D4ED8),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun EmptyBrowseCard(message: String) {
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

