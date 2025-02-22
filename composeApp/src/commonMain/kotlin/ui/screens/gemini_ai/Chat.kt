package ui.screens.gemini_ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(viewModel :AiScreenModel) {
    Scaffold { pv ->
        Column(
            modifier = Modifier.padding(pv).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f)) {}
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.prompt,
                onValueChange = { viewModel.prompt = it }
            )
        }
    }
}