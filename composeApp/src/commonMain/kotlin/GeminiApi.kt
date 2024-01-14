import dev.johnoreilly.gemini.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Part(val text: String? = null, val inline_data: InlineData? = null)

@Serializable
data class InlineData(val mime_type: String, val data: String)

@Serializable
data class Content(val parts: List<Part>)

@Serializable
data class Candidate(val content: Content)

@Serializable
data class Error(val message: String)

@Serializable
data class GenerateContentResponse(val error: Error? = null, val candidates: List<Candidate>? = null)

@Serializable
data class GenerateContentRequest(val contents: Content)

class GeminiApi {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models"
    private val apiKey = BuildKonfig.GEMINI_API_KEY

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = false})
        }
    }

    suspend fun generateContent(prompt: String): GenerateContentResponse {
        val textPart = Part(text = prompt)
        val contents = Content(listOf(textPart))
        val request = GenerateContentRequest(contents)

        return client.post("$baseUrl/gemini-pro:generateContent") {
            contentType(ContentType.Application.Json)
            url { parameters.append("key", apiKey) }
            setBody(request)
        }.body<GenerateContentResponse>()
    }


    suspend fun generateContent(prompt: String, imageData: String): GenerateContentResponse {
        val inlineData = InlineData("image/jpeg", imageData)
        val textPart = Part(text = prompt)
        val imagePart = Part(inline_data = inlineData)
        val contents = Content(listOf(textPart, imagePart))
        val request = GenerateContentRequest(contents)

        return client.post("$baseUrl/gemini-pro-vision:generateContent") {
            contentType(ContentType.Application.Json)
            url { parameters.append("key", apiKey) }
            setBody(request)
        }.body<GenerateContentResponse>()
    }
}