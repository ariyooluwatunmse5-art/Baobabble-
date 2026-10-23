package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
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
import com.example.data.model.AfricanLanguage
import com.example.data.model.AiMessage
import com.example.data.model.MessageSender
import com.example.ui.theme.*
import com.example.util.AiRateLimiter
import com.example.util.RateLimitResult
import com.example.util.SpeechAudioHelper
import kotlinx.coroutines.launch

@Composable
fun AiTutorScreen(
    currentLanguage: AfricanLanguage,
    messages: List<AiMessage>,
    isThinking: Boolean,
    onSendMessage: (String) -> Unit,
    isPremium: Boolean = false,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val speechHelper = remember { SpeechAudioHelper(context) }
    DisposableEffect(Unit) {
        onDispose { speechHelper.shutdown() }
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun dispatchQuestion(text: String) {
        when (val rateCheck = AiRateLimiter.canMakeAiRequest(isPremium)) {
            is RateLimitResult.Allowed -> {
                AiRateLimiter.recordAiRequest()
                onSendMessage(text)
            }
            is RateLimitResult.ExceededPerMinute -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Please wait ${rateCheck.waitSeconds}s before asking another question.")
                }
            }
            is RateLimitResult.ExceededDailyFreeLimit -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Daily free AI limit reached (${rateCheck.dailyMax} queries). Upgrade to Baobab+ for unlimited tutoring!")
                }
            }
        }
    }

    val quickQuestions = remember(currentLanguage) {
        when (currentLanguage) {
            AfricanLanguage.YORUBA -> listOf(
                "How do Yoruba tones work?",
                "When do I use 'Ẹ' for respect?",
                "Balogun market bargaining tips",
                "What is Omoluabi philosophy?"
            )
            AfricanLanguage.SWAHILI -> listOf(
                "Explain the 16 Swahili noun classes",
                "Differences between Habari and Jambo",
                "How is penultimate stress pronounced?",
                "Swahili proverbs for daily life"
            )
            AfricanLanguage.AMHARIC -> listOf(
                "How does the Fidel Ge'ez script work?",
                "Tradition of the Ethiopian Buna ceremony",
                "Polite forms for addressing elders",
                "Common greetings in Addis Ababa"
            )
            AfricanLanguage.IGBO -> listOf(
                "Explain Igbo tone and vowel harmony",
                "The tradition of breaking Kola nut",
                "Formal vs casual Igbo greetings",
                "Deep Igbo cultural proverbs"
            )
            AfricanLanguage.HAUSA -> listOf(
                "Traditional Sahel market etiquette",
                "Hausa greetings throughout the day",
                "Grammatical gender in Hausa nouns",
                "Proverb about patience and humility"
            )
            AfricanLanguage.TWI -> listOf(
                "What Akan day name matches each day?",
                "Meaning of popular Adinkra symbols",
                "Responding to 'Ɛte sɛn?' properly",
                "Ashanti royal court greetings"
            )
            AfricanLanguage.ZULU -> listOf(
                "How do the 3 Zulu click consonants sound?",
                "What is the deeper philosophy of Ubuntu?",
                "Difference between Hamba kahle and Sala kahle",
                "Victoria Street Market in Durban phrases"
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
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
                    Text(text = "🧙🏾‍♂️", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mwalimu Babatunde",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Real Gemini AI African Language Tutor • ${currentLanguage.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BaobabGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (isPremium) "⚡ Unlimited AI Tutor (Baobab+)" else "⏳ ${AiRateLimiter.getDailyRemaining(false)} Free Queries Remaining Today",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isPremium) SavannaGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Quick Prompt Pills
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                SuggestionChip(
                    onClick = {
                        dispatchQuestion(question)
                    },
                    label = { Text(question, fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }

        // Chat Conversation Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(
                    message = msg,
                    language = currentLanguage,
                    onSpeak = { text -> speechHelper.speak(text, currentLanguage) }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = BaobabGreen,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mwalimu is consulting ancestral wisdom & Gemini AI...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState)

        // Input Bar
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
                    placeholder = { Text("Ask Mwalimu about ${currentLanguage.displayName}...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_tutor_input"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BaobabGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val text = inputText
                            inputText = ""
                            dispatchQuestion(text)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BaobabGreen)
                        .testTag("send_ai_tutor_button"),
                    enabled = inputText.isNotBlank() && !isThinking
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: AiMessage,
    language: AfricanLanguage,
    onSpeak: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER
    val bubbleColor = if (isUser) BaobabGreen else MaterialTheme.colorScheme.surface
    val textColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BaobabGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🧙🏾‍♂️", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = bubbleColor,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    lineHeight = 22.sp
                )

                if (message.culturalNote != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isUser) Color.White.copy(alpha = 0.15f) else BaobabGreenContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💡", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = message.culturalNote,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isUser) Color.White else BaobabOnGreenContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (!isUser) {
                    Spacer(modifier = Modifier.height(4.dp))
                    IconButton(
                        onClick = { onSpeak(message.text) },
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = BaobabGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
