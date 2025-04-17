import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import core.app_db.DataManager
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screens.gemini_ai.GeminiAIScreen
import ui.theme.AppTheme

@Preview
@Composable
fun App() {
    val theme by DataManager.getValueFlow(DataManager.THEME_KEY)
        .collectAsState(initial = null)
    AppTheme(
        mode = when (theme?.lowercase()) {
            "dark" -> AppTheme.Dark
            "light" -> AppTheme.Light
            else -> AppTheme.System
        }
    ) {
        Navigator(GeminiAIScreen)
    }
}
