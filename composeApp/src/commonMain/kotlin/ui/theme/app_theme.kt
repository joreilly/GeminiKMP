package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

enum class AppTheme {
    Light,
    Dark,
    System
}

private val lightScheme = lightColorScheme()
private val darkScheme = darkColorScheme()

@Composable
fun AppTheme(
    mode: AppTheme = AppTheme.System,
    content: @Composable () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = remember(mode) {
        when (mode) {
            AppTheme.Light -> lightScheme
            AppTheme.Dark -> darkScheme
            AppTheme.System -> if (isDark) darkScheme else lightScheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
//        typography = MaterialTheme.typography,
        content = content
    )
}