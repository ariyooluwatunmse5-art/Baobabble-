package com.example.data.model

data class AlphabetLetter(
    val letter: String,
    val phoneticIpa: String,
    val pronunciationTip: String,
    val exampleNative: String,
    val exampleTranslation: String,
    val isSpecialDiacritic: Boolean = false
)

data class ToneDetail(
    val name: String,
    val diacriticSymbol: String,
    val solfaNote: String, // e.g., "Mí (High)", "Re (Mid)", "Dò (Low)"
    val pitchDescription: String,
    val audioKey: String,
    val exampleNative: String,
    val exampleMeaning: String
)

data class MinimalToneSet(
    val baseLetters: String,
    val explanation: String,
    val variations: List<ToneVariation>
)

data class ToneVariation(
    val word: String,
    val toneMarks: String,
    val solfaPitches: String,
    val meaning: String,
    val exampleSentence: String
)

data class GrammarRule(
    val id: String,
    val cefrLevel: ProficiencyLevel,
    val title: String,
    val ruleSummary: String,
    val formulaPattern: String,
    val examples: List<GrammarExample>,
    val commonMistake: String
)

data class GrammarExample(
    val nativeSentence: String,
    val wordByWordBreakdown: String,
    val englishTranslation: String
)

data class CulturalContextGreeting(
    val situation: String,
    val nativePhrase: String,
    val literalTranslation: String,
    val englishMeaning: String,
    val politeResponse: String,
    val culturalEtiquette: String
)

data class LinguisticProfile(
    val language: AfricanLanguage,
    val alphabet: List<AlphabetLetter>,
    val toneSystem: List<ToneDetail>,
    val minimalToneSets: List<MinimalToneSet>,
    val greetingsCatalog: List<CulturalContextGreeting>,
    val grammarRules: List<GrammarRule>,
    val culturalPhilosophyTitle: String,
    val culturalPhilosophyText: String,
    val aiTutorLinguisticDirectives: String
)
