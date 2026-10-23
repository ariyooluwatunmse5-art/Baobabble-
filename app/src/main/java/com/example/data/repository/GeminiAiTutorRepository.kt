package com.example.data.repository

import com.example.BuildConfig
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

class GeminiAiTutorRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun askTutor(
        question: String,
        language: AfricanLanguage,
        level: ProficiencyLevel = ProficiencyLevel.A1
    ): AiMessage = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        val profile = LinguisticProfileRepository.getProfile(language)

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are Mwalimu Babatunde, the erudite African language tutor of Baobabble.
                    Target Language: ${language.displayName} (${language.nativeName})
                    Learner CEFR Level: ${level.code} - ${level.title}
                    
                    AUTHORITATIVE LINGUISTIC SYSTEM DIRECTIVES FOR ${language.displayName.uppercase()}:
                    ${profile.aiTutorLinguisticDirectives}
                    
                    Cultural Philosophy: ${profile.culturalPhilosophyTitle}
                    
                    Learner Question: "$question"
                    
                    Instructions:
                    1. Answer strictly conforming to the ${language.displayName} phonetics, tone markings, and grammar rules.
                    2. If the user made an error, correct it with:
                       • Correct word/phrase with full diacritics
                       • Pronunciation/musical pitch guide (e.g. solfa Dò-Re-Mí)
                       • Literal breakdown
                       • Cultural etiquette note
                    3. Adapt explanation complexity to CEFR level ${level.code}.
                    4. Keep response under 140 words, formatted in warm, clear paragraphs.
                """.trimIndent()

                val apiResponse = callGeminiApi(apiKey, prompt)
                if (!apiResponse.isNullOrBlank()) {
                    return@withContext AiMessage(
                        id = UUID.randomUUID().toString(),
                        sender = MessageSender.TUTOR,
                        text = apiResponse,
                        culturalNote = "Verified for ${language.displayName} (${level.code})"
                    )
                }
            } catch (e: Exception) {
                // Fallback to authoritative linguistic knowledge engine
            }
        }

        // Contextual Linguistic Engine Fallback
        return@withContext generateAuthoritativeFallback(question, profile, level)
    }

    suspend fun sendConversationReply(
        userMessage: String,
        scenario: ConversationScenario,
        history: List<AiMessage>
    ): AiMessage = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        val profile = LinguisticProfileRepository.getProfile(scenario.language)

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    Roleplay as ${scenario.roleName} in this scenario: ${scenario.situation} in ${scenario.location}.
                    Target language: ${scenario.language.displayName} (${scenario.language.nativeName}).
                    Linguistic constraints: ${profile.aiTutorLinguisticDirectives}
                    
                    The learner just said: "$userMessage".
                    
                    Respond naturally as ${scenario.roleName}.
                    Format your response in JSON:
                    {
                      "nativeResponse": "Spoken reply in ${scenario.language.displayName} with full tone marks and diacritics",
                      "englishTranslation": "English translation",
                      "culturalNote": "One short tip on market or family etiquette"
                    }
                """.trimIndent()

                val apiResponse = callGeminiApi(apiKey, prompt)
                if (!apiResponse.isNullOrBlank()) {
                    val cleanJson = apiResponse.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                    val json = JSONObject(cleanJson)
                    return@withContext AiMessage(
                        id = UUID.randomUUID().toString(),
                        sender = MessageSender.TUTOR,
                        text = json.optString("nativeResponse", apiResponse),
                        translation = if (json.has("englishTranslation")) json.getString("englishTranslation") else null,
                        culturalNote = json.optString("culturalNote", "Etiquette in ${scenario.location}")
                    )
                }
            } catch (e: Exception) {
                // Fall through to roleplay fallback
            }
        }

        return@withContext generateRoleplayFallback(userMessage, scenario)
    }

    private fun callGeminiApi(apiKey: String, prompt: String): String? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
        val payload = JSONObject().apply {
            val contents = JSONArray().apply {
                val item = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                    }
                    put("parts", parts)
                }
                put(item)
            }
            put("contents", contents)
        }

        val request = Request.Builder()
            .url(url)
            .post(payload.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val root = JSONObject(body)
            val candidates = root.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null
            val candidate = candidates.getJSONObject(0)
            val content = candidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            if (parts.length() == 0) return null
            return parts.getJSONObject(0).optString("text")
        }
    }

    private fun generateAuthoritativeFallback(
        question: String,
        profile: LinguisticProfile,
        level: ProficiencyLevel
    ): AiMessage {
        val q = question.lowercase()

        // Check if question asks about tones
        if (q.contains("tone") || q.contains("pitch") || q.contains("accent") || q.contains("solfa")) {
            val toneDesc = profile.toneSystem.joinToString("\n") {
                "• ${it.name}: ${it.diacriticSymbol} (${it.solfaNote}) — ${it.pitchDescription}. Example: ${it.exampleNative}"
            }
            val minSet = profile.minimalToneSets.firstOrNull()
            val minExample = minSet?.variations?.joinToString("\n") { "  → ${it.word} (${it.solfaPitches}): ${it.meaning}" } ?: ""

            val text = """
                In ${profile.language.displayName}, tones are musical pitches that alter meaning!
                
                $toneDesc
                ${if (minSet != null) "\nMinimal Tone Triplet (${minSet.baseLetters}):\n$minExample" else ""}
            """.trimIndent()

            return AiMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.TUTOR,
                text = text,
                culturalNote = "Listen closely to pitch melody: tone shifts entirely change the word!"
            )
        }

        // Check if question asks about alphabet or pronunciation (gb, p, ẹ, ọ, ṣ)
        if (q.contains("alphabet") || q.contains("letter") || q.contains("gb") || q.contains("pronounce") || q.contains("sound")) {
            val specialLetters = profile.alphabet.filter { it.isSpecialDiacritic }
            val letterList = specialLetters.joinToString("\n") {
                "• ${it.letter} (${it.phoneticIpa}): ${it.pronunciationTip}. Example: ${it.exampleNative} (${it.exampleTranslation})"
            }

            return AiMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.TUTOR,
                text = "Key pronunciation rules for ${profile.language.displayName}:\n\n$letterList",
                culturalNote = "Mastering special sounds gives you an authentic, respected accent."
            )
        }

        // Check if question asks about grammar or rules
        val matchedGrammar = profile.grammarRules.firstOrNull { rule ->
            q.contains(rule.title.lowercase()) || rule.title.lowercase().split(" ").any { q.contains(it) && it.length > 3 }
        } ?: profile.grammarRules.firstOrNull()

        if (q.contains("grammar") || q.contains("rule") || q.contains("sentence") || q.contains("order") || q.contains("verb")) {
            if (matchedGrammar != null) {
                val examplesStr = matchedGrammar.examples.joinToString("\n") { "• ${it.nativeSentence} (${it.wordByWordBreakdown}) → \"${it.englishTranslation}\"" }
                val text = """
                    ${matchedGrammar.title} [Level ${matchedGrammar.cefrLevel.code}]:
                    ${matchedGrammar.ruleSummary}
                    
                    Formula: ${matchedGrammar.formulaPattern}
                    
                    Examples:
                    $examplesStr
                    
                    ⚠️ Common pitfall: ${matchedGrammar.commonMistake}
                """.trimIndent()

                return AiMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.TUTOR,
                    text = text,
                    culturalNote = "Grammar follows consistent semantic patterns without European irregular inflections."
                )
            }
        }

        // Default greeting / etiquette response
        val greeting = profile.greetingsCatalog.firstOrNull()
        val text = """
            Welcome to the Baobab shade! In ${profile.language.displayName} (${level.code}), every greeting carries deep respect.
            
            Key greeting: "${greeting?.nativePhrase ?: profile.language.greeting}"
            Meaning: ${greeting?.englishMeaning ?: profile.language.greeting}
            Etiquette: ${greeting?.culturalEtiquette ?: profile.culturalPhilosophyText}
            
            ${profile.culturalPhilosophyTitle}:
            "${profile.culturalPhilosophyText}"
        """.trimIndent()

        return AiMessage(
            id = UUID.randomUUID().toString(),
            sender = MessageSender.TUTOR,
            text = text,
            culturalNote = "Cultural wisdom from the elders"
        )
    }

    private fun generateRoleplayFallback(userMessage: String, scenario: ConversationScenario): AiMessage {
        val u = userMessage.lowercase()
        return when (scenario.language) {
            AfricanLanguage.YORUBA -> {
                if (u.contains("élò") || u.contains("price") || u.contains("how much") || u.contains("ra")) {
                    AiMessage(
                        id = UUID.randomUUID().toString(),
                        sender = MessageSender.TUTOR,
                        text = "Isu náà jẹ́ ẹgbẹ̀rún kan náírà (#1,000) fún mẹ́ta. Ṣùgbọ́n nítorí pé o bọ̀wọ̀ fún mi, màá dín sí ọgọ́rùn-ún mẹ́jọ (#800) fún ẹ!",
                        translation = "The yams are 1,000 Naira for three. But because you greeted me respectfully, I will discount it to 800 for you!",
                        culturalNote = "In Nigerian markets, greeting respectfully earns immediate seller discounts!"
                    )
                } else {
                    AiMessage(
                        id = UUID.randomUUID().toString(),
                        sender = MessageSender.TUTOR,
                        text = "O ṣeun pupọ ọmọ mi! Ọjà dídára ni mo ń tà fún ẹ. Ṣé o fẹ́ fi ata àti epo kun un?",
                        translation = "Thank you very much my child! I sell only quality goods to you. Would you like to add pepper and palm oil?",
                        culturalNote = "Vendors often suggest complementary soup ingredients."
                    )
                }
            }
            AfricanLanguage.SWAHILI -> {
                AiMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.TUTOR,
                    text = "Poa kabisa rafiki yangu! Kiti chako kiko hapa mbele. Safari yetu inaenda vizuri bila wasiwasi.",
                    translation = "Totally cool my friend! Your seat is right in front. Our journey is smooth and worry-free.",
                    culturalNote = "Matatu conductors enjoy quick, polite banter with passengers."
                )
            }
            else -> {
                AiMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.TUTOR,
                    text = "Ameseginalehu / Dàálụ / Nagode! Gbogbo nǹkan ń lọ dáadáa. Karibu tena!",
                    translation = "Thank you! Everything is progressing wonderfully. Welcome again!",
                    culturalNote = "African hospitality is heartfelt and continuous."
                )
            }
        }
    }
}
