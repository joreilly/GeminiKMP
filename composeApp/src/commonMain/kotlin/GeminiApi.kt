import io.ktor.client.HttpClient
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
data class Part(val text: String)

@Serializable
data class Content(val parts: List<Part>)

@Serializable
data class Candidate(val content: Content)

@Serializable
data class Error(val message: String)

@Serializable
data class GenerateContentResponse(val error: Error? = null, val candidates: List<Candidate>? = null)



class GeminiApi {
    private val baseUrl = " https://generativelanguage.googleapis.com/v1beta/models"

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = false})
        }
    }


    suspend fun generateContent(prompt: String) = client.post("$baseUrl/gemini-pro:generateContent?key=AIzaSyBwiEWF0vJgj-OEwdZWoWn1nUpIz1fd-mA") {
        contentType(ContentType.Application.Json)
        setBody("{\n" +
                "      \"contents\": [{\n" +
                "        \"parts\":[{\n" +
                "          \"text\": \"$prompt\"}]}]}")
    }


}