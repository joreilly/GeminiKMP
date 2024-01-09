package dev.johnoreilly.gemini.common

import dev.johnoreilly.gemini.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
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

@Serializable
data class GenerateContentRequest(val contents: Content)

class GeminiApi {
    private val baseUrl = " https://generativelanguage.googleapis.com/v1beta/models"
    private val apiKey = BuildConfig.GEMINI_API_KEY

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = false})
        }
    }

    suspend fun generateContent(prompt: String): GenerateContentResponse {
        val part = Part(text = prompt)
        val contents = Content(listOf(part))
        val request = GenerateContentRequest(contents)

        return client.post("$baseUrl/gemini-pro:generateContent") {
            contentType(ContentType.Application.Json)
            url { parameters.append("key", apiKey) }
            setBody(request)
        }.body<GenerateContentResponse>()
    }
}