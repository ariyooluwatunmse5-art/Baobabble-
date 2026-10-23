package com.example.data.model

data class UserProfile(
    val id: String = "user_default",
    val name: String = "Amina / Kwame",
    val avatarEmoji: String = "🌱",
    val currentLanguage: AfricanLanguage = AfricanLanguage.YORUBA,
    val currentLevel: ProficiencyLevel = ProficiencyLevel.A1,
    val totalXp: Int = 140,
    val currentStreak: Int = 3,
    val maxStreak: Int = 5,
    val hearts: Int = 5,
    val maxHearts: Int = 5,
    val isPremium: Boolean = false,
    val lastActiveEpochDay: Long = System.currentTimeMillis() / (1000 * 60 * 60 * 24),
    val dailyGoalMinutes: Int = 15,
    val todayMinutesLearned: Int = 8,
    val completedLessonIds: Set<String> = setOf("yoruba_a1_u1_l1"),
    val unlockedAchievementIds: Set<String> = setOf("first_seed", "streak_3")
)

data class WeakWord(
    val id: String,
    val nativeWord: String,
    val englishMeaning: String,
    val languageId: String,
    val mistakeCount: Int,
    val masteryScore: Int = 0, // 0 to 100
    val lastPracticedTimestamp: Long = System.currentTimeMillis()
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false
)

data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val target: Int,
    val current: Int,
    val xpReward: Int,
    val isCompleted: Boolean = false
)

data class OwnerAnalytics(
    val totalRegisteredLearners: Int = 48290,
    val activeLearnersToday: Int = 6720,
    val totalLessonsCompleted: Int = 214800,
    val averageRetentionRatePercent: Int = 78,
    val monthlyEstimatedRevenueUsd: Double = 14250.00,
    val adImpressionsToday: Int = 18450,
    val topLanguages: List<LanguagePopularity> = listOf(
        LanguagePopularity("Yoruba", 32, "🇳🇬"),
        LanguagePopularity("Swahili", 28, "🇰🇪"),
        LanguagePopularity("Amharic", 16, "🇪🇹"),
        LanguagePopularity("Igbo", 11, "🇳🇬"),
        LanguagePopularity("Hausa", 8, "🇳🇬"),
        LanguagePopularity("Twi", 5, "🇬🇭")
    )
)

data class LanguagePopularity(
    val name: String,
    val percent: Int,
    val flag: String
)
