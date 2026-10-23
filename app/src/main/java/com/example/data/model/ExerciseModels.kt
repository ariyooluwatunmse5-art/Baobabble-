package com.example.data.model

data class LearningUnit(
    val id: String,
    val languageId: String,
    val level: ProficiencyLevel,
    val unitNumber: Int,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val unitId: String,
    val order: Int,
    val title: String,
    val topicSummary: String,
    val xpReward: Int = 20,
    val exercises: List<Exercise>
)

sealed class Exercise {
    abstract val id: String
    abstract val instruction: String
    abstract val culturalTip: String?

    data class VocabularyIntro(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val nativeWord: String,
        val phonetic: String,
        val englishMeaning: String,
        val exampleSentence: String,
        val exampleTranslation: String,
        val audioPhrase: String
    ) : Exercise()

    data class MultipleChoice(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val promptText: String,
        val promptSubtext: String? = null,
        val options: List<String>,
        val correctIndex: Int,
        val explanation: String,
        val audioPhrase: String? = null
    ) : Exercise()

    data class MatchingPairs(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val pairs: List<PairItem>
    ) : Exercise()

    data class Translate(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val sourceText: String,
        val wordBank: List<String>,
        val correctTokens: List<String>,
        val expectedTranslation: String,
        val audioPhrase: String? = null
    ) : Exercise()

    data class SentenceBuilder(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val englishMeaning: String,
        val scrambledTokens: List<String>,
        val correctOrder: List<String>,
        val audioPhrase: String? = null
    ) : Exercise()

    data class Listening(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val spokenPhrase: String,
        val phoneticHint: String,
        val options: List<String>,
        val correctIndex: Int
    ) : Exercise()

    data class Speaking(
        override val id: String,
        override val instruction: String,
        override val culturalTip: String? = null,
        val targetPhrase: String,
        val phoneticGuide: String,
        val englishMeaning: String,
        val toneAdvice: String
    ) : Exercise()
}

data class PairItem(
    val nativeWord: String,
    val englishMeaning: String
)

enum class LessonNodeState {
    LOCKED,
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED
}
