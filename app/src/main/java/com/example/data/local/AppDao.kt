package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    // Weak words for spaced review
    @Query("SELECT * FROM weak_words WHERE languageId = :langId ORDER BY mistakeCount DESC")
    fun getWeakWordsFlow(langId: String): Flow<List<WeakWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeakWord(word: WeakWordEntity)

    @Query("DELETE FROM weak_words WHERE id = :wordId")
    suspend fun deleteWeakWord(wordId: String)

    // Chat history
    @Query("SELECT * FROM chat_history WHERE scenarioId = :scenarioId ORDER BY timestamp ASC")
    fun getMessagesForScenarioFlow(scenarioId: String): Flow<List<ChatHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatHistoryEntity)
}
