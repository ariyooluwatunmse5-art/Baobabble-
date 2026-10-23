package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.BaobabContentRepository
import com.example.ui.theme.*
import com.example.util.SpeechAudioHelper

@Composable
fun AiConversationScreen(
    currentLanguage: AfricanLanguage,
    activeScenario: ConversationScenario?,
    messages: List<AiMessage>,
    isThinking: Boolean,
    onStartScenario: (ConversationScenario) -> Unit,
    onSendMessage: (String) -> Unit,
    onExitScenario: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechAudioHelper(context) }
    DisposableEffect(Unit) {
        onDispose { speechHelper.shutdown() }
    }

    if (activeScenario != null) {
        BackHandler { onExitScenario() }
        ScenarioChatView(
            scenario = activeScenario,
            messages = messages,
            isThinking = isThinking,
            speechHelper = speechHelper,
            onSendMessage = onSendMessage,
            onExit = onExitScenario
        )
    } else {
        ScenarioSelectionView(
            currentLanguage = currentLanguage,
            onSelectScenario = onStartScenario,
            modifier = modifier
        )
    }
}

@Composable
private fun ScenarioSelectionView(
    currentLanguage: AfricanLanguage,
    onSelectScenario: (ConversationScenario) -> Unit,
    modifier: Modifier = Modifier
) {
    val scenarios = remember(currentLanguage) {
        BaobabContentRepository.getConversationScenarios(currentLanguage)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.statusBarsPadding()) {
                Text(
                    text = "Realistic Dialogue Practice",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Roleplay with native speakers in markets, transit, cafes and family circles.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(scenarios) { scenario ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectScenario(scenario) }
                    .testTag("scenario_card_${scenario.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BaobabGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = scenario.roleAvatar, fontSize = 26.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = scenario.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "📍 ${scenario.location} • ${scenario.difficulty}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BaobabGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = scenario.situation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onSelectScenario(scenario) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Roleplay", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScenarioChatView(
    scenario: ConversationScenario,
    messages: List<AiMessage>,
    isThinking: Boolean,
    speechHelper: SpeechAudioHelper,
    onSendMessage: (String) -> Unit,
    onExit: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit Dialogue")
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BaobabGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = scenario.roleAvatar, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scenario.roleName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "📍 ${scenario.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Messages Feed
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == MessageSender.USER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isUser) BaobabGreen else MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp,
                        modifier = Modifier.widthIn(max = 290.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                            if (msg.translation != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "🇬🇧 \"${msg.translation}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isUser) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (msg.culturalNote != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💡 ${msg.culturalNote}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUser) SavannaGoldBright else BaobabGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (!isUser) {
                                Spacer(modifier = Modifier.height(4.dp))
                                IconButton(
                                    onClick = { speechHelper.speak(msg.text, scenario.language) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .align(Alignment.End)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak",
                                        tint = BaobabGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Text(
                        text = "${scenario.roleName} is replying...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Suggested Replies Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(scenario.suggestedReplies) { reply ->
                SuggestionChip(
                    onClick = { onSendMessage(reply) },
                    label = { Text(reply, fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = SavannaGoldContainer
                    )
                )
            }
        }

        // Input Box
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Reply in ${scenario.language.displayName}...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val text = inputText
                            inputText = ""
                            onSendMessage(text)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BaobabGreen),
                    enabled = inputText.isNotBlank() && !isThinking
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}
