import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechFactory
import nl.marc_apps.tts.TextToSpeechInstance


class DesktopTextToSpeech : TextToSpeech {
    private var textToSpeech: TextToSpeechInstance? = null

    init {
        CoroutineScope(Dispatchers.Default).launch {
            textToSpeech = TextToSpeechFactory().createOrNull()
        }
    }

    override suspend fun speak(text: String) {
        textToSpeech?.say(text)
    }

    override suspend fun stop() {
        textToSpeech?.stop()
        textToSpeech?.close()
    }
}