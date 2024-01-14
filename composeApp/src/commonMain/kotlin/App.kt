import androidx.compose.foundation.Image
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun App() {
    val api = remember { GeminiApi() }

    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("") }
    var imageData by remember { mutableStateOf<String?>(null) }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }

    var filePath by remember { mutableStateOf("") }

    var image by remember { mutableStateOf<ImageBitmap?>(null) }

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
                OutlinedButton(
                    onClick = {
                        if (prompt.isNotBlank()) {
                            coroutineScope.launch {
                                showProgress = true
                                content = generateContent(api, prompt, imageData)
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

                OutlinedButton(
                    onClick = { showImagePicker = true },
                    modifier = Modifier
                        .weight(3f)
                        .padding(all = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("Select Image")
                }

                ImagePicker(show = showImagePicker) { file, data ->
                    showImagePicker = false
                    filePath = file
                    imageData = data

                    imageData?.let {
                        val decodedBytes = Base64.Default.decode(imageData!!)
                        image = Image.makeFromEncoded(decodedBytes).toComposeImageBitmap()
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            image?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = "",
                    modifier = Modifier.height(200.dp)
                )
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


suspend fun generateContent(api: GeminiApi, prompt: String, imageData: String? = null): String {
    println("prompt = $prompt")
    val result = if (imageData != null) api.generateContent(prompt, imageData)
    else api.generateContent(prompt)
    return if (result.candidates != null) {
        result.candidates[0].content.parts[0].text ?: ""
    } else {
        "No results"
    }
}