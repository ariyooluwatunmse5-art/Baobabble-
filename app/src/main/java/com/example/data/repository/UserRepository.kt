package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.local.UserEntity
import com.example.data.local.WeakWordEntity
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val dao: AppDao) {

    fun getUserProfileFlow(): Flow<UserProfile> {
        return dao.getUserProfileFlow().map { entity ->
            if (entity == null) {
                UserProfile()
            } else {
                UserProfile(
                    id = entity.id,
                    name = entity.name,
                    avatarEmoji = entity.avatarEmoji,
                    currentLanguage = AfricanLanguage.values().firstOrNull { it.id == entity.currentLanguageId } ?: AfricanLanguage.YORUBA,
                    currentLevel = ProficiencyLevel.values().firstOrNull { it.code == entity.currentLevelCode } ?: ProficiencyLevel.A1,
                    totalXp = entity.totalXp,
                    currentStreak = entity.currentStreak,
                    maxStreak = entity.maxStreak,
                    hearts = entity.hearts,
                    maxHearts = entity.maxHearts,
                    isPremium = entity.isPremium,
                    lastActiveEpochDay = entity.lastActiveEpochDay,
                    dailyGoalMinutes = entity.dailyGoalMinutes,
                    todayMinutesLearned = entity.todayMinutesLearned,
                    completedLessonIds = entity.completedLessonsCsv.split(",").filter { it.isNotBlank() }.toSet(),
                    unlockedAchievementIds = entity.unlockedAchievementsCsv.split(",").filter { it.isNotBlank() }.toSet()
                )
            }
        }
    }

    suspend fun ensureInitialUser() {
        val existing = dao.getUserProfile()
        if (existing == null) {
            val initial = UserEntity(
                id = "primary_user",
                name = "Amina / Kwame",
                avatarEmoji = "🌱",
                currentLanguageId = AfricanLanguage.YORUBA.id,
                currentLevelCode = ProficiencyLevel.A1.code,
                totalXp = 140,
                currentStreak = 3,
                maxStreak = 5,
                hearts = 5,
                maxHearts = 5,
                isPremium = false,
                lastActiveEpochDay = currentEpochDay(),
                dailyGoalMinutes = 15,
                todayMinutesLearned = 8,
                completedLessonsCsv = "yoruba_a1_u1_l1",
                unlockedAchievementsCsv = "first_seed,streak_3"
            )
            dao.insertOrUpdateUser(initial)

            // Seed initial weak words for smart review
            dao.upsertWeakWord(
                WeakWordEntity(
                    id = "yo_w1",
                    nativeWord = "Ẹ kú àárọ̀",
                    englishMeaning = "Good morning (respectful)",
                    languageId = AfricanLanguage.YORUBA.id,
                    mistakeCount = 2,
                    masteryScore = 40,
                    lastPracticedTimestamp = System.currentTimeMillis() - 86400000
                )
            )
            dao.upsertWeakWord(
                WeakWordEntity(
                    id = "sw_w1",
                    nativeWord = "Habari ya asubuhi",
                    englishMeaning = "Good morning",
                    languageId = AfricanLanguage.SWAHILI.id,
                    mistakeCount = 1,
                    masteryScore = 60,
                    lastPracticedTimestamp = System.currentTimeMillis() - 172800000
                )
            )
        }
    }

    suspend fun switchLanguage(language: AfricanLanguage) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(currentLanguageId = language.id))
    }

    suspend fun switchLevel(level: ProficiencyLevel) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(currentLevelCode = level.code))
    }

    suspend fun addLessonCompletion(lessonId: String, xpEarned: Int) {
        val current = dao.getUserProfile() ?: return
        val completedSet = current.completedLessonsCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        completedSet.add(lessonId)

        val today = currentEpochDay()
        var streak = current.currentStreak
        if (today > current.lastActiveEpochDay) {
            streak = if (today == current.lastActiveEpochDay + 1) streak + 1 else 1
        }
        val maxStreak = maxOf(streak, current.maxStreak)

        val unlocked = current.unlockedAchievementsCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        if (completedSet.size >= 1) unlocked.add("first_seed")
        if (streak >= 3) unlocked.add("streak_3")

        dao.insertOrUpdateUser(
            current.copy(
                totalXp = current.totalXp + xpEarned,
                currentStreak = streak,
                maxStreak = maxStreak,
                lastActiveEpochDay = today,
                todayMinutesLearned = current.todayMinutesLearned + 4,
                completedLessonsCsv = completedSet.joinToString(","),
                unlockedAchievementsCsv = unlocked.joinToString(",")
            )
        )
    }

    suspend fun decrementHeart(): Int {
        val current = dao.getUserProfile() ?: return 5
        if (current.isPremium) return current.hearts // Unlimited hearts for Baobab+ members
        val remaining = (current.hearts - 1).coerceAtLeast(0)
        dao.insertOrUpdateUser(current.copy(hearts = remaining))
        return remaining
    }

    suspend fun refillHearts() {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(hearts = current.maxHearts))
    }

    suspend fun addXp(xp: Int) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(totalXp = current.totalXp + xp))
    }

    suspend fun updateDailyGoal(minutes: Int) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(dailyGoalMinutes = minutes))
    }

    suspend fun updateProfile(name: String, avatarEmoji: String) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(name = name, avatarEmoji = avatarEmoji))
    }

    suspend fun setPremium(isPremium: Boolean) {
        val current = dao.getUserProfile() ?: return
        dao.insertOrUpdateUser(current.copy(isPremium = isPremium, hearts = if (isPremium) 999 else 5))
    }

    // Spaced repetition & weak words
    fun getWeakWordsFlow(languageId: String): Flow<List<WeakWord>> {
        return dao.getWeakWordsFlow(languageId).map { list ->
            list.map {
                WeakWord(
                    id = it.id,
                    nativeWord = it.nativeWord,
                    englishMeaning = it.englishMeaning,
                    languageId = it.languageId,
                    mistakeCount = it.mistakeCount,
                    masteryScore = it.masteryScore,
                    lastPracticedTimestamp = it.lastPracticedTimestamp
                )
            }
        }
    }

    suspend fun recordMistake(nativeWord: String, englishMeaning: String, languageId: String) {
        val id = "${languageId}_${nativeWord.lowercase().replace(" ", "_")}"
        dao.upsertWeakWord(
            WeakWordEntity(
                id = id,
                nativeWord = nativeWord,
                englishMeaning = englishMeaning,
                languageId = languageId,
                mistakeCount = 1,
                masteryScore = 20,
                lastPracticedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun recordWordMastered(wordId: String) {
        dao.deleteWeakWord(wordId)
    }

    private fun currentEpochDay(): Long {
        return System.currentTimeMillis() / (1000 * 60 * 60 * 24)
    }
}
