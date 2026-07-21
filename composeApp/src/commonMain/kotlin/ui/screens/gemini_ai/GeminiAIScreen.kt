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
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        val theme by viewModel.theme.collectAsState() // For StateFlow
        var showClearDialog by remember { mutableStateOf(false) }


        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        Text(
                            "Gemini",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
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
                        if (viewModel.screen == AiScreenType.Chat) {
                            IconButton(onClick = { showClearDialog = true }) {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    imageVector = Icons.Outlined.DeleteSweep,
                                    contentDescription = "Clear chat"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (getPlatform() is Platform.Android || getPlatform() is Platform.Ios) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = viewModel.screen == AiScreenType.Assistant,
                            onClick = { viewModel.changeScreen(AiScreenType.Assistant) },
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Res.drawable.assistant),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Assistant") }
                        )
                        NavigationBarItem(
                            selected = viewModel.screen == AiScreenType.Chat,
                            onClick = { viewModel.changeScreen(AiScreenType.Chat) },
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Res.drawable.chat),
                                    contentDescription = null
                                )
                            },
                            label = { Text("Chat") }
                        )
                    }
                }
            }
        ) { pv ->
            Row(Modifier.padding(pv).fillMaxSize()) {
                if (getPlatform() is Platform.Desktop || getPlatform() is Platform.Web) {
                    Column(
                        Modifier.fillMaxWidth(0.3f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DropdownMenuItem(
                            enabled = viewModel.screen != AiScreenType.Assistant,
                            text = {
                                Text(
                                    "Assistant",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = { viewModel.changeScreen(AiScreenType.Assistant) },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier.size(50.dp),
                                    painter = painterResource(Res.drawable.assistant),
                                    contentDescription = "AI Assistant Screen"
                                )
                            }
                        )
                        DropdownMenuItem(
                            enabled = viewModel.screen != AiScreenType.Chat,
                            text = { Text("Chat", style = MaterialTheme.typography.bodyMedium) },
                            onClick = { viewModel.changeScreen(AiScreenType.Chat) },
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
                    AiScreenType.Assistant -> AssistantScreen()
                    AiScreenType.Chat -> ChatScreen(viewModel)
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Clear chat?") },
                text = { Text("This will permanently delete the current conversation.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearDatabase()
                        showClearDialog = false
                    }) { Text("Clear") }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}