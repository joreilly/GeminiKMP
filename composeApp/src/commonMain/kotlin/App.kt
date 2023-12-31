import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun App() {
    val api = remember { GeminiApi() }

    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("Summarize the benefits of Kotlin Multiplatform") }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth().padding(16.dp)
        ) {
            Row {
                TextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.weight(7f)
                )
                TextButton(
                    onClick = {
                        if (prompt.isNotBlank()) {
                            coroutineScope.launch {
                                showProgress = true
                                content = generateContent(api, prompt)
                                showProgress = false
                            }
                        }
                    },

                    modifier = Modifier
                        .weight(3f)
                        .padding(all = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("Submit")
                }
            }

            Spacer(Modifier.height(16.dp))
            if (showProgress) {
                CircularProgressIndicator()
            } else {
                Text(content)
            }
        }
    }
}

suspend fun generateContent(api: GeminiApi, prompt: String): String {
    println("prompt = $prompt")
    val result = api.generateContent(prompt)
    return if (result.candidates != null) {
        result.candidates[0].content.parts[0].text
    } else {
        "No results"
    }
}