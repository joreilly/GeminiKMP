package dev.johnoreilly.gemini

import TextToSpeech
import java.util.Locale

class AndroidTextToSpeech : TextToSpeech {
    private val tts = android.speech.tts.TextToSpeech(MainActivity.instance, null)
    init {
        tts.language = Locale.US
    }
    override suspend fun speak(text: String) {
        tts.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override suspend fun stop() {
        tts.stop()
    }
}