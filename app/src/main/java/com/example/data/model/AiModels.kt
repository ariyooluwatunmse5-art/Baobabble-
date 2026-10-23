package com.example.data.model

enum class MessageSender {
    USER,
    TUTOR,
    SYSTEM
}

data class AiMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val translation: String? = null,
    val culturalNote: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ConversationScenario(
    val id: String,
    val language: AfricanLanguage,
    val title: String,
    val situation: String,
    val location: String,
    val roleName: String,
    val roleAvatar: String,
    val difficulty: String,
    val initialMessage: String,
    val initialTranslation: String,
    val suggestedReplies: List<String>
)
