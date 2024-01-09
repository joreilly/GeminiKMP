package dev.johnoreilly.gemini.wear

sealed class Screen(
    val route: String,
) {
    object PromptScreen : Screen("prompt")
}