import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class, ExperimentalLayoutApi::class)
@Composable
fun App() {
    val api = remember { GeminiApi() }

    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("") }
    var selectedImageData by remember { mutableStateOf<ByteArray?>(null) }
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
            FlowRow {
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
                                generateContentAsFlow(api, prompt, selectedImageData).collect {
                                    if (!it.text.isNullOrBlank()) {
                                        showProgress = false
                                    }
                                    content += it.text
                                }
                                println(content)

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

                ImagePicker(show = showImagePicker) { file, imageData ->
                    showImagePicker = false
                    filePath = file
                    selectedImageData = imageData
                    imageData?.let {
                        image = imageData.toComposeImageBitmap()
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
                GeminiMarkdown(content)
            }
        }
    }
}


fun generateContentAsFlow(
    api: GeminiApi,
    prompt: String,
    imageData: ByteArray? = null
): Flow<GenerateContentResponse> {
    return if (imageData != null) {
        api.generateContent(prompt, imageData)
    } else {
        api.generateContent(prompt)
    }
}