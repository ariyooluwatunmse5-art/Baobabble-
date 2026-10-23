package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.BaobabbleViewModel
import com.example.ui.components.AdBreakDialog
import com.example.ui.components.BaobabTopBar
import com.example.ui.components.HeartRefillDialog
import com.example.ui.components.LinguisticSoundboardDialog
import com.example.ui.components.PremiumSubscriptionDialog
import com.example.ui.screens.*
import com.example.ui.theme.BaobabGreen
import com.example.ui.theme.BaobabbleTheme

enum class MainNavTab(val title: String, val icon: ImageVector, val tag: String) {
    LEARN("Learn", Icons.Default.School, "tab_learn"),
    REVIEW("Review", Icons.Default.Refresh, "tab_review"),
    AI_TUTOR("AI Tutor", Icons.Default.AutoAwesome, "tab_ai_tutor"),
    DIALOGUE("Dialogue", Icons.Default.Forum, "tab_dialogue"),
    PROFILE("Profile", Icons.Default.Person, "tab_profile")
}

class MainActivity : ComponentActivity() {

    private val viewModel: BaobabbleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaobabbleTheme {
                BaobabbleApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BaobabbleApp(viewModel: BaobabbleViewModel) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedLevel by viewModel.selectedLevel.collectAsStateWithLifecycle()
    val learningUnits by viewModel.learningUnits.collectAsStateWithLifecycle()
    val weakWords by viewModel.weakWords.collectAsStateWithLifecycle()

    val activeLesson by viewModel.activeLesson.collectAsStateWithLifecycle()
    val currentExerciseIndex by viewModel.currentExerciseIndex.collectAsStateWithLifecycle()
    val isLessonCompleted by viewModel.isLessonCompleted.collectAsStateWithLifecycle()
    val mistakesCount by viewModel.lessonMistakesCount.collectAsStateWithLifecycle()

    val tutorMessages by viewModel.tutorMessages.collectAsStateWithLifecycle()
    val isTutorThinking by viewModel.isTutorThinking.collectAsStateWithLifecycle()

    val activeScenario by viewModel.activeScenario.collectAsStateWithLifecycle()
    val scenarioMessages by viewModel.scenarioMessages.collectAsStateWithLifecycle()
    val isScenarioThinking by viewModel.isScenarioThinking.collectAsStateWithLifecycle()

    val showPremiumDialog by viewModel.showPremiumDialog.collectAsStateWithLifecycle()
    val showHeartRefillDialog by viewModel.showHeartRefillDialog.collectAsStateWithLifecycle()
    val showAdBreakDialog by viewModel.showAdBreakDialog.collectAsStateWithLifecycle()
    val showSoundboard by viewModel.showSoundboard.collectAsStateWithLifecycle()
    val ownerAnalytics by viewModel.ownerAnalytics.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(MainNavTab.LEARN) }
    var isViewingOwnerDashboard by remember { mutableStateOf(false) }

    // If active lesson session is ongoing, display full-screen lesson experience
    if (activeLesson != null) {
        LessonExerciseScreen(
            lesson = activeLesson!!,
            exerciseIndex = currentExerciseIndex,
            isCompleted = isLessonCompleted,
            mistakesCount = mistakesCount,
            currentHearts = userProfile.hearts,
            isPremium = userProfile.isPremium,
            language = selectedLanguage,
            onAnswerSubmitted = { isCorrect, mistake ->
                viewModel.submitAnswer(isCorrect, mistake)
            },
            onNextExercise = { viewModel.nextExercise() },
            onCloseLesson = { viewModel.closeLesson() }
        )
        return
    }

    // Owner Dashboard Overlay
    if (isViewingOwnerDashboard) {
        OwnerDashboardScreen(
            analytics = ownerAnalytics,
            isPremium = userProfile.isPremium,
            onTogglePremium = { viewModel.upgradeToPremium() },
            onRefillHearts = { viewModel.finishRewardedAd() },
            onBack = { isViewingOwnerDashboard = false }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BaobabTopBar(
                userProfile = userProfile,
                currentLanguage = selectedLanguage,
                onLanguageSelected = { viewModel.selectLanguage(it) },
                onHeartsClick = {
                    if (!userProfile.isPremium) viewModel.openHeartRefillDialog() else viewModel.openPremiumDialog()
                },
                onStreakClick = { currentTab = MainNavTab.PROFILE }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_navigation_bar"),
                tonalElevation = 8.dp
            ) {
                MainNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.title)
                        },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BaobabGreen,
                            selectedTextColor = BaobabGreen,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainNavTab.LEARN -> {
                    LearnScreen(
                        currentLanguage = selectedLanguage,
                        currentLevel = selectedLevel,
                        units = learningUnits,
                        userProfile = userProfile,
                        onLevelSelected = { viewModel.selectLevel(it) },
                        onStartLesson = { lesson -> viewModel.startLesson(lesson) },
                        onOpenReview = { currentTab = MainNavTab.REVIEW },
                        onOpenSoundboard = { viewModel.openSoundboard() },
                        onClaimChest = { unitNum -> viewModel.claimChestReward(unitNum) }
                    )
                }
                MainNavTab.REVIEW -> {
                    ReviewScreen(
                        currentLanguage = selectedLanguage,
                        weakWords = weakWords,
                        onMasterWord = { word -> viewModel.masterWeakWord(word) }
                    )
                }
                MainNavTab.AI_TUTOR -> {
                    AiTutorScreen(
                        currentLanguage = selectedLanguage,
                        messages = tutorMessages,
                        isThinking = isTutorThinking,
                        onSendMessage = { query -> viewModel.askGeminiTutor(query) },
                        isPremium = userProfile.isPremium
                    )
                }
                MainNavTab.DIALOGUE -> {
                    AiConversationScreen(
                        currentLanguage = selectedLanguage,
                        activeScenario = activeScenario,
                        messages = scenarioMessages,
                        isThinking = isScenarioThinking,
                        onStartScenario = { scen -> viewModel.startScenario(scen) },
                        onSendMessage = { text -> viewModel.sendScenarioMessage(text) },
                        onExitScenario = { viewModel.exitScenario() }
                    )
                }
                MainNavTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        onOpenPremium = { viewModel.openPremiumDialog() },
                        onOpenOwnerDashboard = { isViewingOwnerDashboard = true },
                        onUpdateProfile = { name, avatar -> viewModel.updateLearnerProfile(name, avatar) },
                        onUpdateDailyGoal = { mins -> viewModel.updateDailyGoal(mins) }
                    )
                }
            }
        }
    }

    // Monetization & Reward Modals
    if (showPremiumDialog) {
        PremiumSubscriptionDialog(
            onDismiss = { viewModel.closePremiumDialog() },
            onSubscribe = { viewModel.upgradeToPremium() }
        )
    }

    if (showHeartRefillDialog) {
        HeartRefillDialog(
            onDismiss = { viewModel.closeHeartRefillDialog() },
            onWatchAd = { viewModel.watchRewardedAd() },
            onOpenPremium = {
                viewModel.closeHeartRefillDialog()
                viewModel.openPremiumDialog()
            }
        )
    }

    if (showAdBreakDialog) {
        AdBreakDialog(
            onDismiss = { /* Non dismissible until finished */ },
            onRewardEarned = { viewModel.finishRewardedAd() }
        )
    }

    if (showSoundboard) {
        LinguisticSoundboardDialog(
            language = selectedLanguage,
            onDismiss = { viewModel.closeSoundboard() }
        )
    }
}
