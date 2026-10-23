package com.example.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import com.example.data.model.AfricanLanguage
import java.util.Locale

class SpeechAudioHelper(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isTtsReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady = true
        }
    }

    fun speak(text: String, language: AfricanLanguage, onFallback: (String) -> Unit = {}) {
        if (!isTtsReady || tts == null) {
            onFallback("Audio phonetic playback: $text")
            return
        }

        val locale = when (language) {
            AfricanLanguage.SWAHILI -> Locale("sw", "KE")
            AfricanLanguage.AMHARIC -> Locale("am", "ET")
            AfricanLanguage.HAUSA -> Locale("ha", "NG")
            AfricanLanguage.YORUBA -> Locale("yo", "NG")
            AfricanLanguage.IGBO -> Locale("ig", "NG")
            AfricanLanguage.TWI -> Locale("ak", "GH")
            AfricanLanguage.ZULU -> Locale("zu", "ZA")
        }

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Honest fallback: notify caller that this specific dialect voice isn't preinstalled on device
            onFallback("Device lacks voice data for ${language.displayName}. Playing phonetic rhythm.")
            tts?.setLanguage(Locale.ENGLISH)
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_${System.currentTimeMillis()}")
    }

    fun startListening(
        language: AfricanLanguage,
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Speech recognition not available on this device. Manual practice active.")
            return
        }

        try {
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, when (language) {
                    AfricanLanguage.SWAHILI -> "sw-KE"
                    AfricanLanguage.AMHARIC -> "am-ET"
                    AfricanLanguage.HAUSA -> "ha-NG"
                    AfricanLanguage.YORUBA -> "yo-NG"
                    AfricanLanguage.IGBO -> "ig-NG"
                    AfricanLanguage.TWI -> "ak-GH"
                    AfricanLanguage.ZULU -> "zu-ZA"
                })
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now in ${language.displayName}")
            }

            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    // Honest fallback
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech match found. Try speaking louder and clearer."
                        SpeechRecognizer.ERROR_NETWORK -> "Network required for ${language.displayName} recognition."
                        else -> "Speech model for ${language.displayName} unavailable on device. Fallback comparison used."
                    }
                    onError(msg)
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        onResult(matches[0])
                    } else {
                        onError("No words detected.")
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            recognizer.startListening(intent)
        } catch (e: Exception) {
            onError("Could not initialize microphone: ${e.message}")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
