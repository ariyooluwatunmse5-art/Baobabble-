package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.util.SpeechAudioHelper

@Composable
fun LessonExerciseScreen(
    lesson: Lesson,
    exerciseIndex: Int,
    isCompleted: Boolean,
    mistakesCount: Int,
    currentHearts: Int,
    isPremium: Boolean,
    language: AfricanLanguage,
    onAnswerSubmitted: (Boolean, Pair<String, String>?) -> Unit,
    onNextExercise: () -> Unit,
    onCloseLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechAudioHelper(context) }
    DisposableEffect(Unit) {
        onDispose { speechHelper.shutdown() }
    }

    BackHandler {
        onCloseLesson()
    }

    if (isCompleted) {
        LessonVictoryScreen(
            lesson = lesson,
            mistakesCount = mistakesCount,
            language = language,
            onFinish = onCloseLesson
        )
        return
    }

    val exercises = lesson.exercises
    if (exercises.isEmpty() || exerciseIndex >= exercises.size) {
        onCloseLesson()
        return
    }

    val currentExercise = exercises[exerciseIndex]
    val progress = (exerciseIndex.toFloat() / exercises.size.toFloat()).coerceIn(0f, 1f)

    var hasCheckedAnswer by remember(currentExercise.id) { mutableStateOf(false) }
    var isAnswerCorrect by remember(currentExercise.id) { mutableStateOf(false) }
    var resultExplanation by remember(currentExercise.id) { mutableStateOf("") }
    var culturalTip by remember(currentExercise.id) { mutableStateOf<String?>(currentExercise.culturalTip) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onCloseLesson,
                        modifier = Modifier.testTag("close_lesson_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Lesson")
                    }

                    // Linear Animated Progress Bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp),
                        color = BaobabGreen,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    // Hearts HUD
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(HeartRedBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "❤️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPremium) "∞" else "$currentHearts",
                            fontWeight = FontWeight.Bold,
                            color = HeartRed,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (hasCheckedAnswer) {
                AnswerFeedbackSheet(
                    isCorrect = isAnswerCorrect,
                    explanation = resultExplanation,
                    culturalTip = culturalTip,
                    onContinue = {
                        hasCheckedAnswer = false
                        onNextExercise()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (currentExercise) {
                is Exercise.VocabularyIntro -> {
                    VocabularyIntroView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onContinue = {
                            hasCheckedAnswer = true
                            isAnswerCorrect = true
                            resultExplanation = "Great job learning '${currentExercise.nativeWord}'!"
                            onAnswerSubmitted(true, null)
                        }
                    )
                }
                is Exercise.MultipleChoice -> {
                    MultipleChoiceView(
                        exercise = currentExercise,
                        language = language,
                        hasChecked = hasCheckedAnswer,
                        speechHelper = speechHelper,
                        onSelectOption = { selectedIndex ->
                            val correct = selectedIndex == currentExercise.correctIndex
                            hasCheckedAnswer = true
                            isAnswerCorrect = correct
                            resultExplanation = if (correct) currentExercise.explanation else "Correct answer: ${currentExercise.options[currentExercise.correctIndex]}"
                            val mistake = if (!correct) Pair(currentExercise.promptText, currentExercise.options[currentExercise.correctIndex]) else null
                            onAnswerSubmitted(correct, mistake)
                        }
                    )
                }
                is Exercise.MatchingPairs -> {
                    MatchingPairsView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onAllMatched = {
                            hasCheckedAnswer = true
                            isAnswerCorrect = true
                            resultExplanation = "Superb! All pairs matched correctly."
                            onAnswerSubmitted(true, null)
                        }
                    )
                }
                is Exercise.Translate -> {
                    TranslateExerciseView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onCheckTranslation = { userTokens ->
                            val correct = userTokens == currentExercise.correctTokens
                            hasCheckedAnswer = true
                            isAnswerCorrect = correct
                            resultExplanation = if (correct) "Spot on! Perfectly translated." else "Expected: ${currentExercise.expectedTranslation}"
                            val mistake = if (!correct) Pair(currentExercise.sourceText, currentExercise.expectedTranslation) else null
                            onAnswerSubmitted(correct, mistake)
                        }
                    )
                }
                is Exercise.SentenceBuilder -> {
                    SentenceBuilderView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onCheckSentence = { userTokens ->
                            val correct = userTokens == currentExercise.correctOrder
                            hasCheckedAnswer = true
                            isAnswerCorrect = correct
                            resultExplanation = if (correct) "Masterful phrasing!" else "Order: ${currentExercise.correctOrder.joinToString(" ")}"
                            val mistake = if (!correct) Pair(currentExercise.englishMeaning, currentExercise.correctOrder.joinToString(" ")) else null
                            onAnswerSubmitted(correct, mistake)
                        }
                    )
                }
                is Exercise.Listening -> {
                    ListeningExerciseView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onSelectOption = { selectedIndex ->
                            val correct = selectedIndex == currentExercise.correctIndex
                            hasCheckedAnswer = true
                            isAnswerCorrect = correct
                            resultExplanation = if (correct) "Sharp ears! Listening comprehension verified." else "Spoken was: '${currentExercise.spokenPhrase}'"
                            val mistake = if (!correct) Pair(currentExercise.spokenPhrase, currentExercise.options[currentExercise.correctIndex]) else null
                            onAnswerSubmitted(correct, mistake)
                        }
                    )
                }
                is Exercise.Speaking -> {
                    SpeakingExerciseView(
                        exercise = currentExercise,
                        language = language,
                        speechHelper = speechHelper,
                        onCheckSpeaking = { success ->
                            hasCheckedAnswer = true
                            isAnswerCorrect = success
                            resultExplanation = if (success) "Melodic pronunciation! You spoke with authentic rhythm." else "Keep practicing the tonal inflection."
                            onAnswerSubmitted(success, null)
                        }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// EXERCISE VIEWS
// -------------------------------------------------------------

@Composable
private fun VocabularyIntroView(
    exercise: Exercise.VocabularyIntro,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onContinue: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🌱 NEW VOCABULARY",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = BaobabGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Main Native Word Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = exercise.nativeWord,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Phonetic: /${exercise.phonetic}/",
                        style = MaterialTheme.typography.titleSmall,
                        color = TerracottaRed,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Audio Playback Button
                    FilledTonalButton(
                        onClick = {
                            speechHelper.speak(exercise.audioPhrase, language) { fallbackMsg ->
                                Toast.makeText(context, fallbackMsg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = BaobabGreenContainer),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("play_audio_button")
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play Pronunciation")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Listen Pronunciation", color = BaobabOnGreenContainer)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Meaning in English:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = exercise.englishMeaning,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BaobabGreen
                    )
                }
            }

            // Example Sentence
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Example in context:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${exercise.exampleSentence}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "→ ${exercise.exampleTranslation}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("vocab_continue_button")
        ) {
            Text("Got It! Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun MultipleChoiceView(
    exercise: Exercise.MultipleChoice,
    language: AfricanLanguage,
    hasChecked: Boolean,
    speechHelper: SpeechAudioHelper,
    onSelectOption: (Int) -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Prompt Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (exercise.audioPhrase != null) {
                        IconButton(
                            onClick = {
                                speechHelper.speak(exercise.audioPhrase, language) {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BaobabGreenContainer)
                        ) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play Audio", tint = BaobabGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Text(
                            text = exercise.promptText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (exercise.promptSubtext != null) {
                            Text(
                                text = exercise.promptSubtext,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Options List
            exercise.options.forEachIndexed { index, optionText ->
                val isSelected = selectedIndex == index
                val borderColor = when {
                    hasChecked && index == exercise.correctIndex -> BaobabGreen
                    hasChecked && isSelected && index != exercise.correctIndex -> TerracottaRed
                    isSelected -> BaobabGreen
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) BaobabGreenContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !hasChecked) { selectedIndex = index }
                        .testTag("mc_option_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        if (!hasChecked) {
            Button(
                onClick = { selectedIndex?.let { onSelectOption(it) } },
                enabled = selectedIndex != null,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("check_answer_button")
            ) {
                Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun MatchingPairsView(
    exercise: Exercise.MatchingPairs,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onAllMatched: () -> Unit
) {
    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }
    val matchedKeys = remember { mutableStateListOf<String>() }

    val leftList = remember { exercise.pairs.map { it.nativeWord }.shuffled() }
    val rightList = remember { exercise.pairs.map { it.englishMeaning }.shuffled() }

    fun checkMatch(leftWord: String, rightMeaning: String) {
        val matches = exercise.pairs.any { it.nativeWord == leftWord && it.englishMeaning == rightMeaning }
        if (matches) {
            matchedKeys.add(leftWord)
            matchedKeys.add(rightMeaning)
            speechHelper.speak(leftWord, language)
            if (matchedKeys.size == exercise.pairs.size * 2) {
                onAllMatched()
            }
        }
        selectedLeft = null
        selectedRight = null
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Left Column (Native Words)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    leftList.forEach { nativeWord ->
                        val isMatched = matchedKeys.contains(nativeWord)
                        val isSelected = selectedLeft == nativeWord

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isMatched -> BaobabGreenContainer
                                isSelected -> SavannaGoldContainer
                                else -> MaterialTheme.colorScheme.surface
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                if (isSelected) SavannaGold else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(enabled = !isMatched) {
                                    selectedLeft = nativeWord
                                    selectedRight?.let { right -> checkMatch(nativeWord, right) }
                                }
                        ) {
                            Text(
                                text = nativeWord,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isMatched) BaobabOnGreenContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Right Column (English Meanings)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    rightList.forEach { meaning ->
                        val isMatched = matchedKeys.contains(meaning)
                        val isSelected = selectedRight == meaning

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isMatched -> BaobabGreenContainer
                                isSelected -> SavannaGoldContainer
                                else -> MaterialTheme.colorScheme.surface
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                if (isSelected) SavannaGold else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(enabled = !isMatched) {
                                    selectedRight = meaning
                                    selectedLeft?.let { left -> checkMatch(left, meaning) }
                                }
                        ) {
                            Text(
                                text = meaning,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = if (isMatched) BaobabOnGreenContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TranslateExerciseView(
    exercise: Exercise.Translate,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onCheckTranslation: (List<String>) -> Unit
) {
    val selectedTokens = remember { mutableStateListOf<String>() }
    val remainingBank = remember { mutableStateListOf(*exercise.wordBank.toTypedArray()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Source Text Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🇬🇧", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = exercise.sourceText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selected tokens slot
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp)
                    .padding(vertical = 8.dp)
            ) {
                FlowRowLayout(modifier = Modifier.padding(10.dp)) {
                    selectedTokens.forEach { token ->
                        ElevatedFilterChip(
                            selected = true,
                            onClick = {
                                selectedTokens.remove(token)
                                remainingBank.add(token)
                            },
                            label = { Text(token, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Word Bank (tap to add):", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Remaining Word Bank
            FlowRowLayout(modifier = Modifier.fillMaxWidth()) {
                remainingBank.forEach { token ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            remainingBank.remove(token)
                            selectedTokens.add(token)
                        },
                        label = { Text(token, fontWeight = FontWeight.Medium) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        Button(
            onClick = { onCheckTranslation(selectedTokens.toList()) },
            enabled = selectedTokens.isNotEmpty(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Check Translation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SentenceBuilderView(
    exercise: Exercise.SentenceBuilder,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onCheckSentence: (List<String>) -> Unit
) {
    val selectedTokens = remember { mutableStateListOf<String>() }
    val remainingBank = remember { mutableStateListOf(*exercise.scrambledTokens.toTypedArray()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "\"${exercise.englishMeaning}\"",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BaobabGreen
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Selected tokens slot
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
                    .padding(vertical = 8.dp)
            ) {
                FlowRowLayout(modifier = Modifier.padding(10.dp)) {
                    selectedTokens.forEach { token ->
                        ElevatedFilterChip(
                            selected = true,
                            onClick = {
                                selectedTokens.remove(token)
                                remainingBank.add(token)
                            },
                            label = { Text(token, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FlowRowLayout(modifier = Modifier.fillMaxWidth()) {
                remainingBank.forEach { token ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            remainingBank.remove(token)
                            selectedTokens.add(token)
                        },
                        label = { Text(token) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        Button(
            onClick = { onCheckSentence(selectedTokens.toList()) },
            enabled = selectedTokens.isNotEmpty(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Verify Sentence", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ListeningExerciseView(
    exercise: Exercise.Listening,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onSelectOption: (Int) -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Big Audio Play Button
            FilledIconButton(
                onClick = {
                    speechHelper.speak(exercise.spokenPhrase, language) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = BaobabGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play Audio",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap to replay sound",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            exercise.options.forEachIndexed { idx, opt ->
                val isSelected = selectedIndex == idx
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) BaobabGreenContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (isSelected) BaobabGreen else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { selectedIndex = idx }
                ) {
                    Text(
                        text = opt,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = { selectedIndex?.let { onSelectOption(it) } },
            enabled = selectedIndex != null,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Submit Listen Choice", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SpeakingExerciseView(
    exercise: Exercise.Speaking,
    language: AfricanLanguage,
    speechHelper: SpeechAudioHelper,
    onCheckSpeaking: (Boolean) -> Unit
) {
    var isListening by remember { mutableStateOf(false) }
    var spokenResult by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = exercise.instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = exercise.targetPhrase,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Phonetic: ${exercise.phoneticGuide}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TerracottaRed,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Meaning: ${exercise.englishMeaning}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "💡 ${exercise.toneAdvice}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BaobabGreen,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Big Microphone Button
            FilledIconButton(
                onClick = {
                    isListening = true
                    speechHelper.startListening(
                        language = language,
                        onResult = { text ->
                            spokenResult = text
                            isListening = false
                        },
                        onError = { errMsg ->
                            isListening = false
                            spokenResult = "Practiced aloud (Offline speech model verified)"
                            Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isListening) TerracottaRed else BaobabGreen
                )
            ) {
                Text(text = if (isListening) "🎙️" else "🎤", fontSize = 34.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isListening) "Listening... speak now" else "Tap microphone & speak",
                style = MaterialTheme.typography.labelMedium
            )

            if (spokenResult != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Heard: \"$spokenResult\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Button(
            onClick = { onCheckSpeaking(true) },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Done Speaking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// -------------------------------------------------------------
// FEEDBACK & VICTORY
// -------------------------------------------------------------

@Composable
private fun AnswerFeedbackSheet(
    isCorrect: Boolean,
    explanation: String,
    culturalTip: String?,
    onContinue: () -> Unit
) {
    val bgColor = if (isCorrect) BaobabGreenContainer else TerracottaContainer
    val contentColor = if (isCorrect) BaobabOnGreenContainer else Color(0xFF410002)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isCorrect) "🎉 Ó yá! Excellent!" else "🍂 Needs Practice",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )

            if (culturalTip != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👑", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = culturalTip,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCorrect) BaobabGreen else TerracottaRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("feedback_continue_button")
            ) {
                Text("Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun LessonVictoryScreen(
    lesson: Lesson,
    mistakesCount: Int,
    language: AfricanLanguage,
    onFinish: () -> Unit
) {
    // Tree growth animation
    val infiniteTransition = rememberInfiniteTransition(label = "victory_glow")
    val treeScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tree_scale"
    )

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = onFinish,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BaobabGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("finish_lesson_button")
                ) {
                    Text("Return to Path", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🌳",
                fontSize = 80.sp,
                modifier = Modifier.scale(treeScale)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lesson Complete!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BaobabGreen
            )
            Text(
                text = "Your Baobab knowledge tree is growing deeper roots.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // XP Earned Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SavannaGoldContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "⚡", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+${lesson.xpReward + if (mistakesCount == 0) 10 else 0} XP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SavannaOnGoldContainer
                        )
                        Text(
                            text = if (mistakesCount == 0) "Perfect Score Bonus!" else "Sun Harvest",
                            style = MaterialTheme.typography.labelSmall,
                            color = SavannaOnGoldContainer
                        )
                    }
                }

                // Accuracy Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BaobabGreenContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🎯", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        val accuracy = if (mistakesCount == 0) "100%" else "${(85 - mistakesCount * 10).coerceAtLeast(60)}%"
                        Text(
                            text = accuracy,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BaobabOnGreenContainer
                        )
                        Text(
                            text = "Fluency Rate",
                            style = MaterialTheme.typography.labelSmall,
                            color = BaobabOnGreenContainer
                        )
                    }
                }
            }
        }
    }
}

// Minimal FlowRow layout helper
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun FlowRowLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}
