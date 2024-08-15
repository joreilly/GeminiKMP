import dev.johnoreilly.gemini.BuildKonfig
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlin.io.encoding.ExperimentalEncodingApi

class GeminiApi {
    companion object {
        const val PROMPT_GENERATE_UI = "Act as an Android app developer. " +
                "For the image provided, use Jetpack Compose to build the screen so that " +
                "the Compose Preview is as close to this image as possible. Also make sure " +
                "to include imports and use Material3. Only give code part without any extra " +
                "text or description neither at start or end, your response should contain " +
                "only code without any explanation."
    }


    private val apiKey = BuildKonfig.GEMINI_API_KEY


    val generativeVisionModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    fun generateContent(prompt: String): Flow<GenerateContentResponse> {
        return generativeModel.generateContentStream(prompt)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateContent(prompt: String, imageData: ByteArray): Flow<GenerateContentResponse> {
        val content = content {
            image(PlatformImage(imageData))
            text(prompt)
        }
        return generativeVisionModel.generateContentStream(content)
    }
}