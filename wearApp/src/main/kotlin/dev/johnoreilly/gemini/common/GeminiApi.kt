package dev.johnoreilly.gemini.common

import dev.johnoreilly.gemini.BuildConfig
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse

class GeminiApi {
    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateContent(prompt: String): GenerateContentResponse {
        return generativeModel.generateContent(prompt)
    }
}