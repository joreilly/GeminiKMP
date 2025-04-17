package ui.screens.gemini_ai

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            scrollState.scrollBy(-delta)
                        }
                    },
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(viewModel.items) { message ->
                val clipboardManager = LocalClipboardManager.current
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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = viewModel.prompt,
            onValueChange = { viewModel.prompt = it },
            trailingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                    viewModel.sendMessage()
                }) {
                    if (viewModel.isLoading is ConnectionState.Loading) {
                        RotatingIcon(painterResource(Res.drawable.rotate_right))
                    } else {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            "Send message"
                        )
                    }
                }
            }
        )
    }
}
