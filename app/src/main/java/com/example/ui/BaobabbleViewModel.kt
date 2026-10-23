package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.BaobabContentRepository
import com.example.data.repository.GeminiAiTutorRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class BaobabbleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val userRepository = UserRepository(db.appDao())
    private val geminiRepository = GeminiAiTutorRepository()

    val userProfile: StateFlow<UserProfile> = userRepository.getUserProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    private val _selectedLanguage = MutableStateFlow(AfricanLanguage.YORUBA)
    val selectedLanguage: StateFlow<AfricanLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedLevel = MutableStateFlow(ProficiencyLevel.A1)
    val selectedLevel: StateFlow<ProficiencyLevel> = _selectedLevel.asStateFlow()

    val learningUnits: StateFlow<List<LearningUnit>> = combine(_selectedLanguage, _selectedLevel) { lang, lvl ->
        BaobabContentRepository.getUnitsForLanguageAndLevel(lang, lvl)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weakWords: StateFlow<List<WeakWord>> = _selectedLanguage.flatMapLatest { lang ->
        userRepository.getWeakWordsFlow(lang.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active lesson session state
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _lessonMistakesCount = MutableStateFlow(0)
    val lessonMistakesCount: StateFlow<Int> = _lessonMistakesCount.asStateFlow()

    private val _isLessonCompleted = MutableStateFlow(false)
    val isLessonCompleted: StateFlow<Boolean> = _isLessonCompleted.asStateFlow()

    // AI Tutor Chat State
    private val _tutorMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage(
                id = "welcome_tutor",
                sender = MessageSender.TUTOR,
                text = "Ẹ n lẹ! Habari! Welcome to the Baobab shade. I am your Gemini AI Tutor. Ask me any question about African grammar, tones, traditions, or greetings!",
                culturalNote = "In traditional African education, elders teach under the great Baobab tree."
            )
        )
    )
    val tutorMessages: StateFlow<List<AiMessage>> = _tutorMessages.asStateFlow()

    private val _isTutorThinking = MutableStateFlow(false)
    val isTutorThinking: StateFlow<Boolean> = _isTutorThinking.asStateFlow()

    // AI Roleplay Conversation State
    private val _activeScenario = MutableStateFlow<ConversationScenario?>(null)
    val activeScenario: StateFlow<ConversationScenario?> = _activeScenario.asStateFlow()

    private val _scenarioMessages = MutableStateFlow<List<AiMessage>>(emptyList())
    val scenarioMessages: StateFlow<List<AiMessage>> = _scenarioMessages.asStateFlow()

    private val _isScenarioThinking = MutableStateFlow(false)
    val isScenarioThinking: StateFlow<Boolean> = _isScenarioThinking.asStateFlow()

    // Monetization & Modals
    private val _showPremiumDialog = MutableStateFlow(false)
    val showPremiumDialog: StateFlow<Boolean> = _showPremiumDialog.asStateFlow()

    private val _showHeartRefillDialog = MutableStateFlow(false)
    val showHeartRefillDialog: StateFlow<Boolean> = _showHeartRefillDialog.asStateFlow()

    private val _showAdBreakDialog = MutableStateFlow(false)
    val showAdBreakDialog: StateFlow<Boolean> = _showAdBreakDialog.asStateFlow()

    // Owner Dashboard State
    val ownerAnalytics = MutableStateFlow(OwnerAnalytics())

    init {
        viewModelScope.launch {
            userRepository.ensureInitialUser()
        }
    }

    fun selectLanguage(language: AfricanLanguage) {
        _selectedLanguage.value = language
        viewModelScope.launch {
            userRepository.switchLanguage(language)
        }
    }

    fun selectLevel(level: ProficiencyLevel) {
        _selectedLevel.value = level
        viewModelScope.launch {
            userRepository.switchLevel(level)
        }
    }

    // -------------------------------------------------------------
    // LESSON PLAYER LIFECYCLE
    // -------------------------------------------------------------
    fun startLesson(lesson: Lesson) {
        val currentHearts = userProfile.value.hearts
        if (currentHearts <= 0 && !userProfile.value.isPremium) {
            _showHeartRefillDialog.value = true
            return
        }

        _activeLesson.value = lesson
        _currentExerciseIndex.value = 0
        _lessonMistakesCount.value = 0
        _isLessonCompleted.value = false
    }

    fun submitAnswer(isCorrect: Boolean, mistakeItem: Pair<String, String>? = null) {
        if (!isCorrect) {
            _lessonMistakesCount.value += 1
            viewModelScope.launch {
                userRepository.decrementHeart()
                if (mistakeItem != null) {
                    userRepository.recordMistake(
                        nativeWord = mistakeItem.first,
                        englishMeaning = mistakeItem.second,
                        languageId = _selectedLanguage.value.id
                    )
                }
            }
        }
    }

    fun nextExercise() {
        val lesson = _activeLesson.value ?: return
        if (_currentExerciseIndex.value + 1 < lesson.exercises.size) {
            _currentExerciseIndex.value += 1
        } else {
            // Lesson Finished
            _isLessonCompleted.value = true
            viewModelScope.launch {
                val earnedXp = lesson.xpReward + (if (_lessonMistakesCount.value == 0) 10 else 0)
                userRepository.addLessonCompletion(lesson.id, earnedXp)
            }
        }
    }

    fun closeLesson() {
        _activeLesson.value = null
        _isLessonCompleted.value = false
    }

    // -------------------------------------------------------------
    // AI TUTOR
    // -------------------------------------------------------------
    // Soundboard State
    private val _showSoundboard = MutableStateFlow(false)
    val showSoundboard: StateFlow<Boolean> = _showSoundboard.asStateFlow()

    fun openSoundboard() { _showSoundboard.value = true }
    fun closeSoundboard() { _showSoundboard.value = false }

    fun claimChestReward(unitNumber: Int) {
        viewModelScope.launch {
            userRepository.addXp(50)
        }
    }

    fun updateDailyGoal(minutes: Int) {
        viewModelScope.launch {
            userRepository.updateDailyGoal(minutes)
        }
    }

    fun updateLearnerProfile(name: String, avatar: String) {
        viewModelScope.launch {
            userRepository.updateProfile(name, avatar)
        }
    }

    // -------------------------------------------------------------
    // AI TUTOR
    // -------------------------------------------------------------
    fun askGeminiTutor(query: String) {
        if (query.isBlank()) return
        val userMsg = AiMessage(
            id = System.currentTimeMillis().toString(),
            sender = MessageSender.USER,
            text = query
        )
        _tutorMessages.value = _tutorMessages.value + userMsg
        _isTutorThinking.value = true

        viewModelScope.launch {
            val response = geminiRepository.askTutor(query, _selectedLanguage.value, _selectedLevel.value)
            _tutorMessages.value = _tutorMessages.value + response
            _isTutorThinking.value = false
        }
    }

    // -------------------------------------------------------------
    // AI CONVERSATION
    // -------------------------------------------------------------
    fun startScenario(scenario: ConversationScenario) {
        _activeScenario.value = scenario
        _scenarioMessages.value = listOf(
            AiMessage(
                id = "scen_init",
                sender = MessageSender.TUTOR,
                text = scenario.initialMessage,
                translation = scenario.initialTranslation,
                culturalNote = "Roleplay with ${scenario.roleName} in ${scenario.location}"
            )
        )
    }

    fun sendScenarioMessage(userText: String) {
        val scenario = _activeScenario.value ?: return
        if (userText.isBlank()) return

        val userMsg = AiMessage(
            id = System.currentTimeMillis().toString(),
            sender = MessageSender.USER,
            text = userText
        )
        _scenarioMessages.value = _scenarioMessages.value + userMsg
        _isScenarioThinking.value = true

        viewModelScope.launch {
            val reply = geminiRepository.sendConversationReply(userText, scenario, _scenarioMessages.value)
            _scenarioMessages.value = _scenarioMessages.value + reply
            _isScenarioThinking.value = false
        }
    }

    fun exitScenario() {
        _activeScenario.value = null
        _scenarioMessages.value = emptyList()
    }

    // -------------------------------------------------------------
    // SMART REVIEW & WEAK WORDS
    // -------------------------------------------------------------
    fun masterWeakWord(word: WeakWord) {
        viewModelScope.launch {
            userRepository.recordWordMastered(word.id)
        }
    }

    // -------------------------------------------------------------
    // MONETIZATION & ADS
    // -------------------------------------------------------------
    fun openPremiumDialog() { _showPremiumDialog.value = true }
    fun closePremiumDialog() { _showPremiumDialog.value = false }

    fun openHeartRefillDialog() { _showHeartRefillDialog.value = true }
    fun closeHeartRefillDialog() { _showHeartRefillDialog.value = false }

    fun upgradeToPremium() {
        viewModelScope.launch {
            userRepository.setPremium(true)
            _showPremiumDialog.value = false
        }
    }

    fun watchRewardedAd() {
        _showAdBreakDialog.value = true
    }

    fun finishRewardedAd() {
        viewModelScope.launch {
            userRepository.refillHearts()
            _showAdBreakDialog.value = false
            _showHeartRefillDialog.value = false
        }
    }
}
