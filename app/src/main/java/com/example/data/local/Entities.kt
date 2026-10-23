package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: String = "primary_user",
    val name: String,
    val avatarEmoji: String,
    val currentLanguageId: String,
    val currentLevelCode: String,
    val totalXp: Int,
    val currentStreak: Int,
    val maxStreak: Int,
    val hearts: Int,
    val maxHearts: Int,
    val isPremium: Boolean,
    val lastActiveEpochDay: Long,
    val dailyGoalMinutes: Int,
    val todayMinutesLearned: Int,
    val completedLessonsCsv: String,
    val unlockedAchievementsCsv: String
)

@Entity(tableName = "weak_words")
data class WeakWordEntity(
    @PrimaryKey val id: String,
    val nativeWord: String,
    val englishMeaning: String,
    val languageId: String,
    val mistakeCount: Int,
    val masteryScore: Int,
    val lastPracticedTimestamp: Long
)

@Entity(tableName = "chat_history")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val scenarioId: String,
    val senderName: String,
    val messageText: String,
    val translation: String?,
    val culturalNote: String?,
    val timestamp: Long
)
