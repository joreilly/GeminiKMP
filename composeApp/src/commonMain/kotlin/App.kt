import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import io.ktor.client.call.body


@Composable
fun App() {
    var content by mutableStateOf("")
    val api = remember { GeminiApi() }

    LaunchedEffect(Unit) {
        val result = api.generateContent("Return the list of people in space. Provide the response as an array of JSON.").body<GenerateContentResponse>()
        println(result)
        if (result.candidates != null) {
            content = result.candidates[0].content.parts[0].text
        } else {
            content = "No results"
        }

    }

    MaterialTheme {
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(content)
        }
    }
}

