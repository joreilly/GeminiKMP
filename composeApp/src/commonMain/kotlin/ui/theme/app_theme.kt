package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    Light,
    Dark,
    System
}

private val lightScheme = lightColorScheme(
    // Gemini-inspired light palette
    primary = Color(0xFF0B57D0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD3E3FD),
    onPrimaryContainer = Color(0xFF041E49),
    secondary = Color(0xFF7C4DFF),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE9DDFF),
    onSecondaryContainer = Color(0xFF1E0059),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F1F1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F1F1F),
    surfaceVariant = Color(0xFFE0E3E7),
    onSurfaceVariant = Color(0xFF45474A),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF)
)

private val darkScheme = darkColorScheme(
    // Gemini-inspired dark palette
    primary = Color(0xFFA8C7FA),
    onPrimary = Color(0xFF102A56),
    primaryContainer = Color(0xFF0842A0),
    onPrimaryContainer = Color(0xFFD3E3FD),
    secondary = Color(0xFFCBB9FF),
    onSecondary = Color(0xFF2E1A64),
    secondaryContainer = Color(0xFF4F378B),
    onSecondaryContainer = Color(0xFFEADDFF),
    background = Color(0xFF0B0F19),
    onBackground = Color(0xFFE6E9EF),
    surface = Color(0xFF0B0F19),
    onSurface = Color(0xFFE6E9EF),
    surfaceVariant = Color(0xFF2A2F3A),
    onSurfaceVariant = Color(0xFFBCC0C7),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)



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
        typography = MaterialTheme.typography,
        content = content
    )
}