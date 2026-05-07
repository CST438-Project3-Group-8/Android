package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyhive_android.data.model.CourseDto
import com.example.studyhive_android.ui.viewmodels.CreateGroupViewModel

@Composable
fun CreateGroupScreen(
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onCreateGroup: () -> Unit,
    createGroupViewModel: CreateGroupViewModel = viewModel()
) {
    var groupName by rememberSaveable { mutableStateOf("") }
    var selectedCourseId by rememberSaveable { mutableStateOf<Int?>(null) }
    var maxMembers by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var meetingMode by rememberSaveable { mutableStateOf("In-Person") }
    var location by rememberSaveable { mutableStateOf("") }
    var schedule by rememberSaveable { mutableStateOf("") }
    val uiState = createGroupViewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.createdGroupId) {
        if (uiState.createdGroupId != null) {
            onCreateGroup()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC) // Very light blue/gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Create a Study Group",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Start a new group to study, collaborate, and prep together.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Basic Information Section
                        SectionHeader(icon = Icons.Outlined.Info, title = "Basic Information")

                        FormField(
                            label = "Group Name",
                            value = groupName,
                            onValueChange = { groupName = it },
                            placeholder = "e.g., Finals Prep - Physics 101"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CourseSelector(
                                    courses = uiState.courses,
                                    selectedCourseId = selectedCourseId,
                                    loading = uiState.loadingCourses,
                                    onCourseSelected = { selectedCourseId = it }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Maximum Members",
                                    value = maxMembers,
                                    onValueChange = { maxMembers = it },
                                    placeholder = "2 - 5 members",
                                    leadingIcon = Icons.Outlined.AccountCircle
                                )
                            }
                        }

                        FormField(
                            label = "Description",
                            value = description,
                            onValueChange = { description = it },
                            placeholder = "Describe what your group will focus on, your goals, and any prerequisites...",
                            singleLine = false,
                            minLines = 4,
                            leadingIcon = Icons.Outlined.Info
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        uiState.error?.let {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF2F2)
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(12.dp),
                                    color = Color(0xFFB91C1C),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        // Meeting Details Section
                        SectionHeader(icon = Icons.Outlined.Notifications, title = "Meeting Details")

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Meeting Mode",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                listOf("In-Person", "Online", "Hybrid").forEach { mode ->
                                    val isSelected = meetingMode == mode
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clickable { meetingMode = mode },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) Color(0xFFEFF6FF) else Color.White,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
                                        )
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = mode,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Location / Link",
                                    value = location,
                                    onValueChange = { location = it },
                                    placeholder = "e.g., Main Library",
                                    leadingIcon = Icons.Outlined.LocationOn
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = "Schedule",
                                    value = schedule,
                                    onValueChange = { schedule = it },
                                    placeholder = "e.g., Thursdays 4:00 PM",
                                    leadingIcon = Icons.Outlined.Notifications
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.height(52.dp).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Button(
                    onClick = {
                        createGroupViewModel.createGroup(
                            title = groupName,
                            description = description,
                            courseId = selectedCourseId,
                            location = location,
                            meetingMode = meetingMode,
                            maxMembers = maxMembers.toIntOrNull()
                        )
                    },
                    enabled = !uiState.submitting,
                    modifier = Modifier
                        .height(52.dp)
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text(
                        text = if (uiState.submitting) "Creating..." else "Create Group",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CourseSelector(
    courses: List<CourseDto>,
    selectedCourseId: Int?,
    loading: Boolean,
    onCourseSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = courses.firstOrNull { it.id == selectedCourseId }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Course or Subject",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Box {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable(enabled = !loading) { expanded = true },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selected?.let { "${it.code} - ${it.title}" }
                            ?: if (loading) "Loading courses..." else "Select a course",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selected == null) Color(0xFF94A3B8) else Color(0xFF0F172A),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 320.dp)
            ) {
                if (courses.isEmpty() && !loading) {
                    DropdownMenuItem(
                        text = { Text("No courses found") },
                        onClick = { expanded = false },
                        enabled = false
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text("No course") },
                        onClick = {
                            onCourseSelected(null)
                            expanded = false
                        }
                    )
                    courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text("${course.code} - ${course.title}") },
                            onClick = {
                                onCourseSelected(course.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                focusedBorderColor = Color(0xFFE2E8F0),
                unfocusedBorderColor = Color(0xFFE2E8F0),
            )
        )
    }
}
