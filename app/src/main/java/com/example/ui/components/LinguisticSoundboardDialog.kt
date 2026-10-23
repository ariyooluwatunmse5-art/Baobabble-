package com.example.ui.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AfricanLanguage
import com.example.data.model.LinguisticProfile
import com.example.data.repository.LinguisticProfileRepository
import com.example.ui.theme.*
import com.example.util.SpeechAudioHelper

enum class SoundboardTab(val title: String) {
    ALPHABET("Alphabet"),
    TONES("Tones & Pitches"),
    GRAMMAR("Grammar Rules"),
    GREETINGS("Etiquette & Greetings")
}

@Composable
fun LinguisticSoundboardDialog(
    language: AfricanLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechAudioHelper(context) }
    DisposableEffect(Unit) {
        onDispose { speechHelper.shutdown() }
    }

    val profile = remember(language) { LinguisticProfileRepository.getProfile(language) }
    var selectedTab by remember { mutableStateOf(SoundboardTab.ALPHABET) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = language.flagEmoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${language.displayName} Linguistic Architecture",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Authoritative phonetics, tones & grammar",
                                style = MaterialTheme.typography.labelSmall,
                                color = BaobabGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_soundboard_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Switcher Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SoundboardTab.values()) { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) BaobabGreen else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedTab = tab }
                                .testTag("soundboard_tab_${tab.name}")
                        ) {
                            Text(
                                text = tab.title,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(14.dp))

                // Content View
                when (selectedTab) {
                    SoundboardTab.ALPHABET -> AlphabetSection(profile = profile, speechHelper = speechHelper)
                    SoundboardTab.TONES -> TonesSection(profile = profile, speechHelper = speechHelper)
                    SoundboardTab.GRAMMAR -> GrammarSection(profile = profile)
                    SoundboardTab.GREETINGS -> GreetingsSection(profile = profile, speechHelper = speechHelper)
                }
            }
        }
    }
}

@Composable
private fun AlphabetSection(
    profile: LinguisticProfile,
    speechHelper: SpeechAudioHelper
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BaobabGreenContainer)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Phonetic Soundboard",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BaobabOnGreenContainer
                    )
                    Text(
                        text = "Tap any letter to hear authentic pronunciation. Special letters with sub-dots or digraphs (Gb, P, Ẹ, Ọ, Ṣ) define the melodic identity of ${profile.language.displayName}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BaobabOnGreenContainer
                    )
                }
            }
        }

        items(profile.alphabet) { letterItem ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (letterItem.isSpecialDiacritic) SavannaGoldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { speechHelper.speak(letterItem.exampleNative, profile.language) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (letterItem.isSpecialDiacritic) SavannaGold else BaobabGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letterItem.letter,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = if (letterItem.isSpecialDiacritic) Color.Black else BaobabOnGreenContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "IPA: ${letterItem.phoneticIpa}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaRed
                            )
                            if (letterItem.isSpecialDiacritic) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "★ Special Sound",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BaobabGreen
                                )
                            }
                        }
                        Text(
                            text = letterItem.pronunciationTip,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Example: \"${letterItem.exampleNative}\" (${letterItem.exampleTranslation})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { speechHelper.speak(letterItem.exampleNative, profile.language) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = BaobabGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TonesSection(
    profile: LinguisticProfile,
    speechHelper: SpeechAudioHelper
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "THE MUSICAL TONE SYSTEM",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = BaobabGreen
            )
        }

        items(profile.toneSystem) { tone ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SavannaGoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tone.solfaNote,
                            fontWeight = FontWeight.ExtraBold,
                            color = SavannaOnGoldContainer,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tone.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Marking: ${tone.diacriticSymbol}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TerracottaRed,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tone.pitchDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Example: ${tone.exampleNative} (${tone.exampleMeaning})",
                            style = MaterialTheme.typography.labelSmall,
                            color = BaobabGreen
                        )
                    }
                }
            }
        }

        // Minimal Tone Sets (e.g. Ọkọ vs Ọkọ̀ vs Ọkọ́)
        if (profile.minimalToneSets.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "MINIMAL TONE PAIRS (Same spelling, completely different meanings!)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TerracottaRed
                )
            }

            items(profile.minimalToneSets) { set ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Root: ${set.baseLetters.uppercase()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = set.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        set.variations.forEach { variation ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { speechHelper.speak(variation.word, profile.language) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = variation.word,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 17.sp,
                                                color = BaobabGreen
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "[${variation.solfaPitches}]",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = TerracottaRed
                                            )
                                        }
                                        Text(
                                            text = "Meaning: ${variation.meaning}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "\"${variation.exampleSentence}\"",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(onClick = { speechHelper.speak(variation.word, profile.language) }) {
                                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play tone", tint = BaobabGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrammarSection(profile: LinguisticProfile) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BaobabGreenContainer)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = profile.culturalPhilosophyTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BaobabOnGreenContainer
                    )
                    Text(
                        text = profile.culturalPhilosophyText,
                        style = MaterialTheme.typography.bodySmall,
                        color = BaobabOnGreenContainer
                    )
                }
            }
        }

        items(profile.grammarRules) { rule ->
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
                            text = rule.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BaobabGreenContainer
                        ) {
                            Text(
                                text = rule.cefrLevel.code,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BaobabOnGreenContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = rule.ruleSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "Pattern: ${rule.formulaPattern}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BaobabGreen,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    rule.examples.forEach { ex ->
                        Text(
                            text = "• ${ex.nativeSentence} (${ex.wordByWordBreakdown})",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "  → \"${ex.englishTranslation}\"",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ Watch out: ${rule.commonMistake}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TerracottaRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun GreetingsSection(
    profile: LinguisticProfile,
    speechHelper: SpeechAudioHelper
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "SITUATIONAL GREETINGS & RESPECT ETIQUETTE",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = BaobabGreen
            )
        }

        items(profile.greetingsCatalog) { greeting ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { speechHelper.speak(greeting.nativePhrase, profile.language) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = greeting.situation,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BaobabGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = greeting.nativePhrase,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { speechHelper.speak(greeting.nativePhrase, profile.language) }) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Listen", tint = BaobabGreen)
                        }
                    }

                    Text(
                        text = "Meaning: ${greeting.englishMeaning} (Literal: \"${greeting.literalTranslation}\")",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Response: \"${greeting.politeResponse}\"",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SavannaGoldContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "👑 Etiquette: ${greeting.culturalEtiquette}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SavannaOnGoldContainer,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
