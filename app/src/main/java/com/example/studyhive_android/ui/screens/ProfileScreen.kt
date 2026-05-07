package com.example.studyhive_android.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    displayName: String,
    email: String,
    onBack: () -> Unit,
    onSecurityClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
                Text("← Back", color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            }

            Text("Your Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            Text("Manage your account settings and personal information.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF64748B))

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontal tab row (sidebar becomes chips on mobile)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                SidebarChip(Icons.Outlined.Person, "General", true) {}
                SidebarChip(Icons.Outlined.Info, "Courses", false) {}
                SidebarChip(Icons.Outlined.Lock, "Security", false, onClick = onSecurityClick)
                SidebarChip(Icons.Outlined.Settings, "Preferences", false) {}
            }

            Spacer(modifier = Modifier.height(20.dp))
            ProfileHeaderCard(displayName)
            Spacer(modifier = Modifier.height(16.dp))
            PersonalDetailsCard(displayName, email)
            Spacer(modifier = Modifier.height(16.dp))
            MyCoursesCard()
            Spacer(modifier = Modifier.height(16.dp))
            DeleteAccountCard()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SidebarChip(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B), modifier = Modifier.size(15.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B))
        }
    }
}

@Composable
private fun ProfileHeaderCard(displayName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box {
                Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFFE2E8F0)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color(0xFF94A3B8))
                }
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd).size(24.dp).clip(CircleShape).background(Color(0xFF2563EB)).border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                Text("Computer Science", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Senior CS student. I love building web apps and studying algorithms.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B), lineHeight = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                Text("Edit", color = Color(0xFF0F172A), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PersonalDetailsCard(displayName: String, email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Personal Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            }
            ProfileFormField("Full Name", displayName)
            ProfileFormField("Email Address", email)
            ProfileFormField("Major / Program", "Computer Science")
            ProfileFormField("Bio", "Senior CS student. I love building web apps and studying algorithms. Always up for a late-night study session before finals!", singleLine = false, minLines = 3)
            Button(onClick = {}, modifier = Modifier.align(Alignment.End), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyCoursesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("My Courses", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("CS301", "MATH220", "PHYS101", "ENG101").forEach { CourseChip(it) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = "", onValueChange = {},
                    placeholder = { Text("Add a course (e.g. HIST 201)", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8FAFC), unfocusedContainerColor = Color(0xFFF8FAFC), unfocusedBorderColor = Color(0xFFE2E8F0))
                )
                Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))) {
                    Text("Add", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CourseChip(name: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFEFF6FF), border = BorderStroke(1.dp, Color(0xFFDBEAFE))) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
            Text("×", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2563EB), modifier = Modifier.clickable { })
        }
    }
}

@Composable
private fun DeleteAccountCard() {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, Color(0xFFFEE2E2))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("🗑️  Delete Account", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF991B1B))
            Text("Once you delete your account, there is no going back. Please be certain.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB91C1C))
            OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFFEE2E2))) {
                Text("Delete Account", color = Color(0xFFB91C1C), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileFormField(label: String, value: String, singleLine: Boolean = true, minLines: Int = 1) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        OutlinedTextField(
            value = value, onValueChange = {}, modifier = Modifier.fillMaxWidth(),
            readOnly = true, singleLine = singleLine, minLines = minLines, shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF8FAFC), unfocusedContainerColor = Color(0xFFF8FAFC), focusedBorderColor = Color(0xFFE2E8F0), unfocusedBorderColor = Color(0xFFE2E8F0))
        )
    }
}