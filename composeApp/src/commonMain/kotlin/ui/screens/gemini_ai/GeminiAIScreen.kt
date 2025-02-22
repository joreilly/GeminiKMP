package ui.screens.gemini_ai

import Platform
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import geminikmp.composeapp.generated.resources.Res
import geminikmp.composeapp.generated.resources.assistant
import geminikmp.composeapp.generated.resources.chat
import geminikmp.composeapp.generated.resources.moon
import geminikmp.composeapp.generated.resources.sun
import getPlatform
import org.jetbrains.compose.resources.painterResource

object GeminiAIScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { AiScreenModel() }
        var expand by mutableStateOf(false)
        val theme by viewModel.theme.collectAsState() // For StateFlow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gemini") },
                    actions = {
                        IconButton(onClick = { viewModel.updateTheme() }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = if (theme?.lowercase() == "dark")
                                    painterResource(Res.drawable.moon)
                                else
                                    painterResource(Res.drawable.sun),
                                contentDescription = null
                            )
                        }
                        if (getPlatform() is Platform.Android || getPlatform() is Platform.Ios) {
                            IconButton(onClick = { expand = !expand }) {
                                Icon(Icons.Default.MoreVert, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = expand,
                                onDismissRequest = { expand = !expand },
                                content = {
                                    DropdownMenuItem(
                                        enabled = viewModel.screen != AiScreen.Assistant,
                                        text = { Text("Assistant") },
                                        onClick = { viewModel.changeScreen(AiScreen.Assistant) },
                                        leadingIcon = {
                                            Icon(
                                                modifier = Modifier.size(25.dp),
                                                painter = painterResource(Res.drawable.assistant),
                                                contentDescription = "AI Assistant Screen"
                                            )
                                        }
                                    )
                                    DropdownMenuItem(
                                        enabled = viewModel.screen != AiScreen.Chat,
                                        text = { Text("Chat") },
                                        onClick = { viewModel.changeScreen(AiScreen.Chat) },
                                        leadingIcon = {
                                            Image(
                                                modifier = Modifier.size(25.dp),
                                                painter = painterResource(Res.drawable.chat),
                                                contentDescription = "AI Chat screen"
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        ) { pv ->
            Row(Modifier.fillMaxSize().padding(pv)) {
                if (getPlatform() is Platform.Desktop || getPlatform() is Platform.Web) {
                    Column(
                        Modifier.fillMaxWidth(0.3f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Assistant") },
                            onClick = { viewModel.changeScreen(AiScreen.Assistant) },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier.size(50.dp),
                                    painter = painterResource(Res.drawable.assistant),
                                    contentDescription = "AI Assistant Screen"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Chat") },
                            onClick = { viewModel.changeScreen(AiScreen.Chat) },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier.size(50.dp),
                                    painter = painterResource(Res.drawable.chat),
                                    contentDescription = "Chat Assistant Screen"
                                )
                            }
                        )
                    }
                    VerticalDivider()
                }
                when (viewModel.screen) {
                    AiScreen.Assistant -> AsistantScreen()
                    AiScreen.Chat -> ChatScreen(viewModel)
                }
            }
        }
    }
}