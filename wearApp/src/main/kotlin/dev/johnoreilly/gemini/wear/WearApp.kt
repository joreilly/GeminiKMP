package dev.johnoreilly.gemini.wear

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.compose.layout.AppScaffold
import dev.johnoreilly.gemini.wear.prompt.GeminiPromptScreen


@Composable
fun WearApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberSwipeDismissableNavController(),
) {
    AppScaffold(modifier = modifier) {
        SwipeDismissableNavHost(
            startDestination = Screen.PromptScreen.route,
            navController = navController,
        ) {
            composable(
                route = Screen.PromptScreen.route,
            ) {
                GeminiPromptScreen()
            }
        }
    }
}