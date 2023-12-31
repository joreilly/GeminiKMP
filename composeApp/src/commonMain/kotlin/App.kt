import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.johnoreilly.gemini.BuildKonfig
import io.ktor.client.call.body


@Composable
fun App() {
    var content by mutableStateOf("")
    val api = remember { GeminiApi() }

    LaunchedEffect(Unit) {
        val result =
            api.generateContent("What is Kotlin Multiplatform").body<GenerateContentResponse>()
        println(result)
        content = if (result.candidates != null) {
            result.candidates[0].content.parts[0].text
        } else {
            "No results"
        }
    }

    MaterialTheme {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(content)
        }
    }
}

