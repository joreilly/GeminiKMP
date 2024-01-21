package dev.johnoreilly.gemini.wear.prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.horologist.ai.ui.model.FailedResponseUiModel
import com.google.android.horologist.ai.ui.model.ModelInstanceUiModel
import com.google.android.horologist.ai.ui.model.PromptOrResponseUiModel
import com.google.android.horologist.ai.ui.model.TextPromptUiModel
import com.google.android.horologist.ai.ui.model.TextResponseUiModel
import com.google.android.horologist.ai.ui.screens.PromptUiState
import dev.johnoreilly.gemini.common.GeminiApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeminiPromptViewModel : ViewModel() {
    val api = GeminiApi()

    val previousQuestions: MutableStateFlow<List<PromptOrResponseUiModel>> =
        MutableStateFlow(listOf())
    val pendingQuestion: MutableStateFlow<TextPromptUiModel?> =
        MutableStateFlow(null)

    fun askQuestion(enteredPrompt: String) {
        val textPromptUiModel = TextPromptUiModel(enteredPrompt)
        pendingQuestion.value = textPromptUiModel

        viewModelScope.launch {
            val responseUi = queryForPrompt(enteredPrompt)

            previousQuestions.update {
                it + listOf(textPromptUiModel, responseUi)
            }

            pendingQuestion.value = null
        }
    }

    private suspend fun queryForPrompt(
        enteredPrompt: String,
    ): PromptOrResponseUiModel {
        return try {
            val text = api.generateContent(enteredPrompt).text ?: error("No results")
            TextResponseUiModel(text)
        } catch (e: Exception) {
            FailedResponseUiModel(e.toString())
        }
    }

    val uiState: StateFlow<PromptUiState> =
        combine(
            previousQuestions,
            pendingQuestion,
        ) { prev, curr ->
            val modelInfo = ModelInstanceUiModel("gemini", "Gemini")
            PromptUiState(modelInfo, prev, curr)
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PromptUiState(messages = previousQuestions.value),
        )
}
