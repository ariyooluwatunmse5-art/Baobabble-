package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AfricanLanguage
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun BaobabTopBar(
    userProfile: UserProfile,
    currentLanguage: AfricanLanguage,
    onLanguageSelected: (AfricanLanguage) -> Unit,
    onHeartsClick: () -> Unit,
    onStreakClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }

    // Pulsing animation for streak flame
    val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language Selector Button
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { languageMenuExpanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("language_selector_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentLanguage.flagEmoji,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentLanguage.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Language",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false }
                ) {
                    AfricanLanguage.values().forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = lang.flagEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = lang.displayName,
                                            fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal,
                                            color = if (lang == currentLanguage) BaobabGreen else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = lang.nativeName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                languageMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Gamification HUD: Streak, XP, Hearts
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Flame
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(StreakFlameBg)
                        .clickable { onStreakClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("streak_indicator"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 15.sp,
                        modifier = Modifier.scale(flameScale)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.currentStreak}",
                        fontWeight = FontWeight.Bold,
                        color = StreakFlame,
                        fontSize = 14.sp
                    )
                }

                // XP Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SavannaGoldContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("xp_indicator"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.totalXp}",
                        fontWeight = FontWeight.Bold,
                        color = SavannaOnGoldContainer,
                        fontSize = 14.sp
                    )
                }

                // Hearts Indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(HeartRedBg)
                        .clickable { onHeartsClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("hearts_indicator"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "❤️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (userProfile.isPremium) "∞" else "${userProfile.hearts}",
                        fontWeight = FontWeight.Bold,
                        color = HeartRed,
                        fontSize = 14.sp
                    )
                    if (!userProfile.isPremium && userProfile.hearts < userProfile.maxHearts) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Refill Hearts",
                            tint = HeartRed,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
