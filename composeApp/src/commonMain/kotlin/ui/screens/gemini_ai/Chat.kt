package ui.screens.gemini_ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import geminikmp.composeapp.generated.resources.Res
import geminikmp.composeapp.generated.resources.rotate_right
import org.jetbrains.compose.resources.painterResource
import ui.ChatBubble
import ui.RotatingIcon
import utils.ConnectionState

@Composable
fun ChatScreen(viewModel: AiScreenModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(viewModel.items) { message ->
                ChatBubble(Modifier.fillMaxWidth(), aiMessage = message)
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
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
