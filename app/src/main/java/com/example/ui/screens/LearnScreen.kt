package com.example.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    currentLanguage: AfricanLanguage,
    currentLevel: ProficiencyLevel,
    units: List<LearningUnit>,
    userProfile: UserProfile,
    onLevelSelected: (ProficiencyLevel) -> Unit,
    onStartLesson: (Lesson) -> Unit,
    onOpenReview: () -> Unit,
    onOpenSoundboard: () -> Unit,
    onClaimChest: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var chestRewardDialogUnit by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. Language Hero & Cultural Greeting Banner
        item {
            LanguageHeroBanner(currentLanguage = currentLanguage)
        }

        // 2. CEFR Level Selector Pill Row (A1 - C2)
        item {
            LevelSelectorRow(
                currentLevel = currentLevel,
                onLevelSelected = onLevelSelected
            )
        }

        // 3. Interactive Linguistic Soundboard Button Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onOpenSoundboard() }
                    .testTag("linguistic_soundboard_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BaobabGreenContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎵", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${currentLanguage.displayName} Alphabet, Tones & Grammar",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleSmall,
                            color = BaobabOnGreenContainer
                        )
                        Text(
                            text = "Interactive soundboard, Solfa musical pitches (Dò-Re-Mí), & rules",
                            style = MaterialTheme.typography.bodySmall,
                            color = BaobabOnGreenContainer.copy(alpha = 0.85f)
                        )
                    }
                    Button(
                        onClick = onOpenSoundboard,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen)
                    ) {
                        Text("Explore", fontSize = 12.sp)
                    }
                }
            }
        }

        // 4. Spaced Repetition / Weak Words Quick Drill
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onOpenReview() }
                    .testTag("quick_review_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🧠", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Smart Spaced Review",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Strengthen weak words and tones before they fade",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = onOpenReview,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen)
                    ) {
                        Text("Review", fontSize = 12.sp)
                    }
                }
            }
        }

        // 4. Learning Units & Winding Path
        items(units) { unit ->
            UnitSection(
                unit = unit,
                userProfile = userProfile,
                onStartLesson = onStartLesson,
                onClaimChest = { unitNum ->
                    chestRewardDialogUnit = unitNum
                    onClaimChest(unitNum)
                }
            )
        }
    }

    if (chestRewardDialogUnit != null) {
        AlertDialog(
            onDismissRequest = { chestRewardDialogUnit = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎁", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Unit $chestRewardDialogUnit Milestone Chest!")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Sun Harvest Reward: +50 XP awarded to your journey!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = BaobabGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"Àgbájọ ọwọ́ la fi ń sọ̀yà\" — When hands unite in a fist, strength is multiplied. Your roots into ${currentLanguage.displayName} are flourishing!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { chestRewardDialogUnit = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen)
                ) {
                    Text("Claim & Rejoice")
                }
            }
        )
    }
}

@Composable
private fun LanguageHeroBanner(currentLanguage: AfricanLanguage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(BaobabGreen, BaobabGreenLight, TerracottaRed)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentLanguage.flagEmoji,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${currentLanguage.displayName} (${currentLanguage.nativeName})",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentLanguage.greeting,
                        style = MaterialTheme.typography.headlineSmall,
                        color = SavannaGoldBright,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            // Cultural Proverb
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📜", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "\"${currentLanguage.sampleProverb}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = currentLanguage.proverbTranslation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelSelectorRow(
    currentLevel: ProficiencyLevel,
    onLevelSelected: (ProficiencyLevel) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        items(ProficiencyLevel.values()) { level ->
            val isSelected = level == currentLevel
            val bgColor = if (isSelected) BaobabGreen else MaterialTheme.colorScheme.surface
            val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = bgColor,
                shadowElevation = if (isSelected) 3.dp else 1.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onLevelSelected(level) }
                    .testTag("level_chip_${level.code}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = level.code,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) SavannaGoldBright else BaobabGreen,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = level.title,
                        color = textColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun UnitSection(
    unit: LearningUnit,
    userProfile: UserProfile,
    onStartLesson: (Lesson) -> Unit,
    onClaimChest: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Unit Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                BaobabGreenContainer.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BaobabGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = unit.iconEmoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "UNIT ${unit.unitNumber} • ${unit.level.code}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BaobabGreen
                    )
                    Text(
                        text = unit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = unit.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duolingo-style Winding Lesson Nodes
        val offsets = listOf(0.dp, 40.dp, (-40).dp, 20.dp, (-20).dp)
        unit.lessons.forEachIndexed { index, lesson ->
            val isCompleted = userProfile.completedLessonIds.contains(lesson.id)
            val isUnlocked = index == 0 || userProfile.completedLessonIds.contains(unit.lessons[index - 1].id)

            val horizontalOffset = offsets[index % offsets.size]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(x = horizontalOffset)
                ) {
                    LessonNodeButton(
                        lesson = lesson,
                        isCompleted = isCompleted,
                        isUnlocked = isUnlocked,
                        onClick = {
                            if (isUnlocked) onStartLesson(lesson)
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }

        // Milestone Chest at end of unit
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = SavannaGoldContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable { onClaimChest(unit.unitNumber) }
                        .testTag("unit_chest_${unit.unitNumber}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🎁", fontSize = 30.sp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unit ${unit.unitNumber} Mastery Chest (Tap to Claim)",
                    style = MaterialTheme.typography.labelSmall,
                    color = SavannaOnGoldContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LessonNodeButton(
    lesson: Lesson,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    // Pulse animation if currently available and uncompleted
    val infiniteTransition = rememberInfiniteTransition(label = "node_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isUnlocked && !isCompleted) 1.07f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val nodeColor = when {
        isCompleted -> BaobabGreen
        isUnlocked -> SavannaGold
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = Modifier
            .size(72.dp)
            .scale(pulseScale)
            .shadow(
                elevation = if (isUnlocked) 6.dp else 1.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(nodeColor)
            .border(
                width = if (isUnlocked && !isCompleted) 4.dp else 2.dp,
                color = if (isUnlocked && !isCompleted) SavannaGoldBright else Color.Transparent,
                shape = CircleShape
            )
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("lesson_node_${lesson.id}"),
        contentAlignment = Alignment.Center
    ) {
        when {
            isCompleted -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            isUnlocked -> {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Start Lesson",
                    tint = Color.Black,
                    modifier = Modifier.size(34.dp)
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
