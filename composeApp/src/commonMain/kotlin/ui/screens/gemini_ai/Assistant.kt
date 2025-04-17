package ui.screens.gemini_ai

import GeminiApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.coil.AsyncImage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssistantScreen() {
    val api = remember { GeminiApi() }
    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<PlatformFile?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val canClearPrompt by remember {
        derivedStateOf {
            prompt.isNotBlank()
        }
    }

    val imagePickerLauncher = rememberFilePickerLauncher(FileKitType.Image) { image ->
        coroutineScope.launch {
            selectedImage = image
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 15.dp)
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 52.dp),
                label = {
                    Text("Search")
                },
                trailingIcon = {
                    if (canClearPrompt) {
                        IconButton(
                            onClick = { prompt = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )

            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    if (prompt.isNotBlank()) {
                        keyboardController?.hide()
                        coroutineScope.launch {
                            println("prompt = $prompt")
                            content = ""
                            generateContentAsFlow(api, prompt, selectedImage?.readBytes())
                                .onStart { showProgress = true }
                                .onCompletion { showProgress = false }
                                .collect {
                                    println("response = ${it.text}")
                                    content += it.text
                                }
                        }
                    }
                },
                enabled = prompt.isNotBlank(),
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Submit",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = { imagePickerLauncher.launch() },
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Select Image",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    prompt = GeminiApi.PROMPT_GENERATE_UI
                    coroutineScope.launch {
                        content = ""
                        generateContentAsFlow(api, prompt, selectedImage?.readBytes())
                            .onStart { showProgress = true }
                            .onCompletion { showProgress = false }
                            .collect {
                                println("response = ${it.text}")
                                content += it.text
                            }
                    }

                },
                enabled = selectedImage != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Generate Compose UI Code",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        selectedImage?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    file = selectedImage,
                    contentDescription = "search_image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        if (showProgress) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            SelectionContainer {
                Markdown(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    content = content
                )
            }
        }
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