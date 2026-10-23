package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.repository.BaobabContentRepository
import com.example.ui.components.AdPlacementContainer
import com.example.ui.components.WebPortalDialog
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onOpenPremium: () -> Unit,
    onOpenOwnerDashboard: () -> Unit,
    onUpdateProfile: (String, String) -> Unit = { _, _ -> },
    onUpdateDailyGoal: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allAchievements = remember { BaobabContentRepository.getAllAchievements() }
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showWebPortalDialog by remember { mutableStateOf(false) }
    var editedName by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var selectedAvatar by remember(userProfile.avatarEmoji) { mutableStateOf(userProfile.avatarEmoji) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(BaobabGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = userProfile.avatarEmoji, fontSize = 36.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${userProfile.currentLanguage.displayName} • Level ${userProfile.currentLevel.code} (${userProfile.currentLevel.title})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BaobabGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showEditProfileDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Text("Edit Name & Avatar", fontSize = 12.sp)
                    }
                }
            }
        }

        // Daily Learning Goal Picker
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Learning Goal",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${userProfile.dailyGoalMinutes} min/day",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = BaobabGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5 to "Casual", 15 to "Regular", 30 to "Scholar").forEach { (mins, label) ->
                            val isSelected = userProfile.dailyGoalMinutes == mins
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BaobabGreen else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onUpdateDailyGoal(mins) }
                                    .testTag("goal_chip_$mins")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) SavannaGoldBright else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total XP",
                    value = "${userProfile.totalXp}",
                    icon = "⚡",
                    containerColor = SavannaGoldContainer,
                    contentColor = SavannaOnGoldContainer,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Day Streak",
                    value = "${userProfile.currentStreak}",
                    icon = "🔥",
                    containerColor = StreakFlameBg,
                    contentColor = StreakFlame,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Lessons",
                    value = "${userProfile.completedLessonIds.size}",
                    icon = "📚",
                    containerColor = BaobabGreenContainer,
                    contentColor = BaobabOnGreenContainer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Baobab+ Membership Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenPremium() }
                    .testTag("premium_card_profile"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SavannaGold, Color(0xFFC9842A), TerracottaRed)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 26.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (userProfile.isPremium) "Baobab+ Active Member" else "Upgrade to Baobab+",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = if (userProfile.isPremium) "Unlimited hearts & offline access" else "Unlimited hearts • No ads • Unlimited AI tutor",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        Button(
                            onClick = onOpenPremium,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (userProfile.isPremium) "Manage" else "Join",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Sponsored Cultural Partner Ad (Non-intrusive, only shown if not premium)
        item {
            AdPlacementContainer(
                isPremium = userProfile.isPremium,
                onUpgradeClick = onOpenPremium
            )
        }

        // Achievements Showcase
        item {
            Text(
                text = "ACHIEVEMENTS",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(allAchievements) { ach ->
            val isUnlocked = userProfile.unlockedAchievementIds.contains(ach.id)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isUnlocked) SavannaGoldContainer else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isUnlocked) ach.iconEmoji else "🔒", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ach.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = ach.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "+${ach.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) BaobabGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Owner / Executive Dashboard Entrance
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPinDialog = true }
                    .testTag("owner_dashboard_button"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Owner Dashboard",
                        tint = BaobabGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Owner & Verified Analytics",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Live telemetry, users, lesson stats and verified revenue",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "ADMIN",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BaobabGreen
                    )
                }
            }
        }

        // Public Website & Legal Portal (SEO guides, Privacy, Terms)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showWebPortalDialog = true }
                    .testTag("open_web_portal_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Public Web Portal",
                        tint = BaobabEmerald,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Official Web Portal & Legal Terms",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Browse public landing page, SEO language guides, Privacy & Terms",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "EXPLORE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BaobabEmerald
                    )
                }
            }
        }
    }

    // Web Portal Dialog
    if (showWebPortalDialog) {
        WebPortalDialog(
            initialPage = "index.html",
            onDismissRequest = { showWebPortalDialog = false }
        )
    }

    // Owner PIN Protection Dialog (Default PIN: 1234 or direct unlock)
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                enteredPin = ""
                pinError = false
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = BaobabGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Owner Security PIN")
                }
            },
            text = {
                Column {
                    Text(
                        "Enter administrator security PIN to inspect real-time metrics, database architecture, and verified revenue.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            enteredPin = it
                            pinError = false
                        },
                        placeholder = { Text("Enter PIN (Default: 1234)") },
                        singleLine = true,
                        isError = pinError,
                        modifier = Modifier.fillMaxWidth().testTag("owner_pin_input")
                    )
                    if (pinError) {
                        Text("Incorrect PIN. Use 1234 or tap Quick Access below.", color = TerracottaRed, style = MaterialTheme.typography.labelSmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredPin == "1234" || enteredPin.isBlank()) {
                            showPinDialog = false
                            onOpenOwnerDashboard()
                        } else {
                            pinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
                    modifier = Modifier.testTag("submit_pin_button")
                ) {
                    Text("Unlock Dashboard")
                }
            },
        )
    }

    // Edit Profile Modal
    if (showEditProfileDialog) {
        val avatarOptions = listOf("🌱", "👑", "🦁", "🌳", "⚡", "🦅", "🥁", "📜")
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Learner Profile") },
            text = {
                Column {
                    Text("Learner Display Name:", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_name_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Select Avatar:", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        avatarOptions.forEach { emoji ->
                            val isChosen = selectedAvatar == emoji
                            Surface(
                                shape = CircleShape,
                                color = if (isChosen) BaobabGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isChosen) androidx.compose.foundation.BorderStroke(2.dp, BaobabGreen) else null,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .clickable { selectedAvatar = emoji }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editedName.ifBlank { "Learner" }, selectedAvatar)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
                    modifier = Modifier.testTag("save_profile_button")
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}
