package ui.screens.gemini_ai

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app_db.DataStoreManager
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


enum class AiScreen {
    Assistant,
    Chat
}

class AiScreenModel : ScreenModel {
    var prompt by mutableStateOf("")
    var screen by mutableStateOf(AiScreen.Assistant)

    // Expose theme as StateFlow for Compose compatibility
    val theme: StateFlow<String?> = DataStoreManager.getValueFlow(DataStoreManager.THEME_KEY)
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null // Provide a default value or fallback
        )
//        val theme by viewModel.theme.collectAsState() // For StateFlow

    // Optional: Expose as Compose State (for direct Compose use)
    val themeState: State<String?>
        get() = mutableStateOf(theme.value)

    // Function to update theme
    fun updateTheme() {
        screenModelScope.launch(Dispatchers.Main) {
            val newTheme = if (themeState.value?.lowercase() == "dark") "light" else "dark"
            DataStoreManager.setValue(DataStoreManager.THEME_KEY, newTheme)
        }
    }

    fun changeScreen(screen: AiScreen) {
        this.screen = screen
    }

}