package ui.screens.gemini_ai

import GeminiApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.app_db.ChatDbManager
import core.app_db.DataManager
import core.models.ChatMessage
import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.type.content
import getTextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import utils.ConnectionState

//data class AiMessage(
//    val id: Long? = null,
//    val aiModel: String,
//    val message: String,
//    val time: String = ""
//)

enum class AiScreenType {
    Assistant,
    Chat
}


class AiScreenModel : ScreenModel {
    var prompt by mutableStateOf("")
    var screen by mutableStateOf(AiScreenType.Assistant)
    var isLoading by mutableStateOf<ConnectionState>(ConnectionState.Default)
    val textToSpeech = getTextToSpeech()

    private val geminiApi = GeminiApi()

    // Expose theme as StateFlow for Compose compatibility
    val theme: StateFlow<String?> = DataManager.getValueFlow(DataManager.THEME_KEY)
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null // Provide a default value or fallback
        )
    var items by mutableStateOf(emptyList<ChatMessage>())
        private set
    private var userChat: Chat by mutableStateOf(geminiApi.generateChat(items))

    init {
        screenModelScope.launch(Dispatchers.Main) {
            items = ChatDbManager.getObjectToStores()
            userChat = geminiApi.generateChat(items)
        }
    }

    fun sendMessage() {
        screenModelScope.launch(Dispatchers.Main) {
            isLoading = ConnectionState.Loading
            try {
                var nowTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                items = items + ChatMessage(
                    sender = "user",
                    message = prompt,
                    time = "${nowTime.date} // ${nowTime.hour}:${nowTime.minute}:${nowTime.second}"
                )
                ChatDbManager.insertObjectToStore(items.last())
                val result = userChat.sendMessage(content("user") { text(prompt) })
                nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                items = items + ChatMessage(
                    sender = "model",
                    message = result.text ?: "Sorry! could not get a response",
                    time = "${nowTime.date} // ${nowTime.hour}:${nowTime.minute}:${nowTime.second}"
                )
                ChatDbManager.insertObjectToStore(items.last())
                prompt = ""
                isLoading = ConnectionState.Success("Success")
            } catch (e: Exception) {
                val nowTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                isLoading = ConnectionState.Error("Could not generate a response")
                items = items + ChatMessage(
                    sender = "model",
                    message = "Sorry! could not get a response $e",
                    time = "${nowTime.date} // ${nowTime.hour}:${nowTime.minute}:${nowTime.second}"
                )
                ChatDbManager.insertObjectToStore(items.last())
                println("Error: $e")
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

    fun clearDatabase() {
        screenModelScope.launch(Dispatchers.Main) {
            ChatDbManager.deleteAllObjectFromStore()
            items = emptyList()
        }
    }

    fun changeScreen(screen: AiScreenType) {
        this.screen = screen
    }
}