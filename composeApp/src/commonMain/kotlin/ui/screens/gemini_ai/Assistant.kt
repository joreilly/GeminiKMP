package ui.screens.gemini_ai

import GeminiApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mikepenz.markdown.m3.Markdown
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@Composable
fun AssistantScreen() {
    val api = remember { GeminiApi() }
    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<PlatformFile?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val canClearPrompt by remember { derivedStateOf { prompt.isNotBlank() } }

    val imagePickerLauncher = rememberFilePickerLauncher(FileKitType.Image) { image ->
        coroutineScope.launch { selectedImage = image }
    }

    fun submit(promptText: String) {
        if (promptText.isBlank()) return
        keyboardController?.hide()
        coroutineScope.launch {
            content = ""
            generateContentAsFlow(api, promptText, selectedImage?.readBytes())
                .onStart { showProgress = true }
                .onCompletion { showProgress = false }
                .catch { content = "Error: ${it.message}" }
                .collect { content += it.text }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .onPreviewKeyEvent { e ->
                    if ((e.key == Key.Enter || e.key == Key.NumPadEnter) &&
                        e.type == KeyEventType.KeyDown && !e.isShiftPressed
                    ) {
                        submit(prompt)
                        true
                    } else {
                        false
                    }
                },
            label = { Text("Enter a prompt") },
            placeholder = { Text("Ask Gemini anything…") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { submit(prompt) }),
            trailingIcon = {
                if (canClearPrompt) {
                    IconButton(onClick = { prompt = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { submit(prompt) },
                enabled = prompt.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Submit", fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = { imagePickerLauncher.launch() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Select Image", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                prompt = GeminiApi.PROMPT_GENERATE_UI
                submit(GeminiApi.PROMPT_GENERATE_UI)
            },
            enabled = selectedImage != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Compose UI Code", fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                showProgress -> CircularProgressIndicator()
                content.isBlank() && selectedImage == null -> EmptyState()
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    selectedImage?.let { image ->
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = image,
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 240.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    if (content.isNotBlank()) {
                        SelectionContainer {
                            Markdown(
                                content = content,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Ask Gemini anything to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun generateContentAsFlow(
    api: GeminiApi,
    prompt: String,
    imageData: ByteArray? = null
): Flow<GenerateContentResponse> = imageData?.let { imageByteArray ->
    api.generateContent(prompt, imageByteArray)
} ?: run {
    api.generateContent(prompt)
}
