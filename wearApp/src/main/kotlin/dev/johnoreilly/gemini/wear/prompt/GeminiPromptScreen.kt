package dev.johnoreilly.gemini.wear.prompt

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme
import com.google.android.horologist.ai.ui.components.PromptOrResponseDisplay
import com.google.android.horologist.ai.ui.model.PromptOrResponseUiModel
import com.google.android.horologist.ai.ui.model.TextResponseUiModel
import com.google.android.horologist.ai.ui.screens.PromptScreen
import com.google.android.horologist.ai.ui.screens.PromptUiState
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberColumnState
import com.google.android.horologist.compose.material.Button
import com.mikepenz.markdown.compose.Markdown
import dev.johnoreilly.gemini.R
import dev.johnoreilly.gemini.wear.markdown.wearMaterialColors
import dev.johnoreilly.gemini.wear.markdown.wearMaterialTypography

@Composable
fun GeminiPromptScreen(
    modifier: Modifier = Modifier,
    viewModel: GeminiPromptViewModel = viewModel(),
    columnState: ScalingLazyColumnState = rememberColumnState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val voiceLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            it.data?.let { data ->
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val enteredPrompt = results?.get(0)
                if (!enteredPrompt.isNullOrBlank()) {
                    viewModel.askQuestion(enteredPrompt)
                }
            }
        }

    val voiceIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )

        putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            stringResource(R.string.prompt_input),
        )
    }

    GeminiPromptScreen(
        uiState = uiState,
        modifier = modifier,
        columnState = columnState,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                Icons.Default.Mic,
                contentDescription = stringResource(R.string.prompt_input),
                onClick = {
                    voiceLauncher.launch(voiceIntent)
                },
            )
        }
    }
}

@Composable
private fun GeminiPromptScreen(
    uiState: PromptUiState,
    modifier: Modifier = Modifier,
    columnState: ScalingLazyColumnState = rememberColumnState(),
    promptEntry: @Composable () -> Unit,
) {
    ScreenScaffold(scrollState = columnState) {
        PromptScreen(
            uiState = uiState,
            modifier = modifier,
            promptEntry = promptEntry,
            promptDisplay = { GeminiPromptDisplay(it) }
        )
    }
}

@Composable
private fun GeminiPromptDisplay(it: PromptOrResponseUiModel) {
    if (it is TextResponseUiModel) {
        GeminiTextResponseCard(it)
    } else {
        PromptOrResponseDisplay(
            promptResponse = it,
            onClick = {},
        )
    }
}

@Composable
fun GeminiTextResponseCard(
    textResponseUiModel: TextResponseUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            MaterialTheme.colors.surface,
            MaterialTheme.colors.surface,
        ),
    ) {
        Markdown(
            textResponseUiModel.text,
            colors = wearMaterialColors(),
            typography = wearMaterialTypography(),
        )
    }
}
