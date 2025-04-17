import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance




class IosTextToSpeech : TextToSpeech {
    private val synthesizer = AVSpeechSynthesizer()

    override suspend fun speak(text: String) {
        val utterance = AVSpeechUtterance.speechUtteranceWithString(text)
//        utterance.voice = AVSpeechSynthesisVoice().voiceWithLanguage("en-US")
        synthesizer.speakUtterance(utterance)
    }

    override suspend fun stop() {
        synthesizer.stopSpeakingAtBoundary(platform.AVFAudio.AVSpeechBoundary.AVSpeechBoundaryWord)
    }
}