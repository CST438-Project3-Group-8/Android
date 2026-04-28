package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CreateGroupScreen(
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onCreateGroup: () -> Unit = {}
) {
    var groupName by rememberSaveable { mutableStateOf("") }
    var course by rememberSaveable { mutableStateOf("") }
    var maxMembers by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var schedule by rememberSaveable { mutableStateOf("") }
    var meetingMode by rememberSaveable { mutableStateOf("In-Person") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        CreateGroupHeader(onBack = onBack)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create a Study Group",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Start a new group to study, collaborate, and prep together.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(18.dp))

                CreateGroupFieldLabel("Group Name")
                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    placeholder = { Text("e.g., Finals Prep - Physics 101") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Course or Subject")
                OutlinedTextField(
                    value = course,
                    onValueChange = { course = it },
                    placeholder = { Text("e.g., PHYS 101") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Maximum Members")
                OutlinedTextField(
                    value = maxMembers,
                    onValueChange = { maxMembers = it },
                    placeholder = { Text("e.g., 2 - 5 members") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Description")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Describe what your group will focus on, your goals, and any prerequisites...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Meeting Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Meeting Mode")
                MeetingModeSelector(
                    selectedMode = meetingMode,
                    onModeSelected = { meetingMode = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Location / Link")
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    placeholder = { Text("e.g., Main Library") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateGroupFieldLabel("Schedule")
                OutlinedTextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    placeholder = { Text("e.g., Thursdays 4:00 PM") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(
                    text = "Cancel",
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onCreateGroup,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text("Create Group")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun CreateGroupHeader(
    onBack: () -> Unit
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

        TextButton(onClick = onBack) {
            Text(
                text = "Back",
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun CreateGroupFieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF334155),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun MeetingModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MeetingModeButton(
                text = "In-Person",
                selected = selectedMode == "In-Person",
                modifier = Modifier.weight(1f),
                onClick = { onModeSelected("In-Person") }
            )

            MeetingModeButton(
                text = "Online",
                selected = selectedMode == "Online",
                modifier = Modifier.weight(1f),
                onClick = { onModeSelected("Online") }
            )
        }

        MeetingModeButton(
            text = "Hybrid",
            selected = selectedMode == "Hybrid",
            modifier = Modifier.fillMaxWidth(),
            onClick = { onModeSelected("Hybrid") }
        )
    }
}

@Composable
private fun MeetingModeButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (selected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
    val backgroundColor = if (selected) Color(0xFFEFF6FF) else Color.White
    val textColor = if (selected) Color(0xFF2563EB) else Color(0xFF475569)

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}