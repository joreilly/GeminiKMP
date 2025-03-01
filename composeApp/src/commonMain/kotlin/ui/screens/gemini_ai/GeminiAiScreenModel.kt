package ui.screens.gemini_ai

import GeminiApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app_db.DataManager
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import utils.ConnectionState

data class AiMessage(
    val aiModel: String,
    val message: String
)

enum class AiScreenType {
    Assistant,
    Chat
}


class AiScreenModel : ScreenModel {
    var prompt by mutableStateOf("")
    var screen by mutableStateOf(AiScreenType.Assistant)
    var isLoading by mutableStateOf<ConnectionState>(ConnectionState.Default)
    private val geminiApi = GeminiApi()

    // Expose theme as StateFlow for Compose compatibility
    val theme: StateFlow<String?> = DataManager.getValueFlow(DataManager.THEME_KEY)
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null // Provide a default value or fallback
        )
    var items by mutableStateOf<List<AiMessage>>(emptyList())
        private set
    private val userChat: Chat = geminiApi.generateChat(items)

    fun sendMessage() {
        screenModelScope.launch(Dispatchers.Main) {
            isLoading = ConnectionState.Loading
            try {

                items = items + AiMessage("user", prompt)
                val result = userChat.sendMessage(content("user") { text(prompt) })
                println("Result: $result")
                items = items + AiMessage("model", result.text ?: "Could not generate a response")
                prompt = ""
                isLoading = ConnectionState.Success("Success")
            } catch (e: Exception) {
                isLoading = ConnectionState.Error("Could not generate a response")
                println("Error: ${e.message}")
                prompt = ""
            }
        }
    }


    // Function to update theme
    fun updateTheme() {
        screenModelScope.launch(Dispatchers.Main) {
            val newTheme = if (theme.value?.lowercase() == "dark") "light" else "dark"
            DataManager.setValue(DataManager.THEME_KEY, newTheme)
        }
    }

    fun changeScreen(screen: AiScreenType) {
        this.screen = screen
    }

}