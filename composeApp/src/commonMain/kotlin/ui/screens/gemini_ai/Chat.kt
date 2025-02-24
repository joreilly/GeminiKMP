package ui.screens.gemini_ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import geminikmp.composeapp.generated.resources.Res
import geminikmp.composeapp.generated.resources.rotate_right
import org.jetbrains.compose.resources.painterResource
import ui.RotatingIcon
import ui.TextIcon
import utils.ConnectionState

@OptIn(ExperimentalComposeUiApi::class)
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
                val align = if (message.aiModel.lowercase() == "user") Arrangement.End else Arrangement.Start
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = align
                ) {
                    TextIcon(
                        Modifier.fillMaxWidth(0.8f),
                        text = {
                            Markdown(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(Color.DarkGray, shape = RoundedCornerShape(5.dp))
                                    .padding(8.dp),
                                content = message.message
                            )
                        },
                        hArrangement = align
                    )
                }
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
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
