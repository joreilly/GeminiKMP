package ui.screens.gemini_ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import geminikmp.composeapp.generated.resources.Res
import geminikmp.composeapp.generated.resources.rotate_right
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import showAlert
import ui.ChatBubble
import ui.RotatingIcon
import utils.ConnectionState


@Composable
fun ChatScreen(viewModel: AiScreenModel) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboardManager.current
    val isLoading = viewModel.isLoading is ConnectionState.Loading

    fun send() {
        if (viewModel.prompt.isBlank() || isLoading) return
        keyboardController?.hide()
        viewModel.sendMessage()
    }

    // Keep the latest message in view as the conversation grows.
    LaunchedEffect(viewModel.items.size) {
        if (viewModel.items.isNotEmpty()) {
            scrollState.animateScrollToItem(viewModel.items.lastIndex)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (viewModel.items.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Start a conversation with Gemini",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.items) { message ->
                        ChatBubble(Modifier.fillMaxWidth(), chatMessage = message) {
                            if (it.first.trim().lowercase() == "copy") {
                                clipboardManager.setText(
                                    annotatedString = buildAnnotatedString {
                                        append(text = it.second)
                                    }
                                )
                                showAlert("Copied to clipboard")
                            } else if (it.first.trim().lowercase() == "speak") {
                                scope.launch(Dispatchers.Default) {
                                    viewModel.textToSpeech.speak(it.second.replace("*", ""))
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.prompt,
                    onValueChange = { viewModel.prompt = it },
                    modifier = Modifier
                        .weight(1f)
                        .onPreviewKeyEvent { e ->
                            if ((e.key == Key.Enter || e.key == Key.NumPadEnter) &&
                                e.type == KeyEventType.KeyDown && !e.isShiftPressed
                            ) {
                                send()
                                true
                            } else {
                                false
                            }
                        },
                    placeholder = { Text("Message Gemini…") },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { send() })
                )
                Spacer(Modifier.width(8.dp))
                FilledIconButton(
                    onClick = { send() },
                    enabled = viewModel.prompt.isNotBlank() && !isLoading,
                    modifier = Modifier.size(52.dp)
                ) {
                    if (isLoading) {
                        RotatingIcon(painterResource(Res.drawable.rotate_right), sizeDpSize = 24.dp)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
                    }
                }
            }
        }
    }
}
